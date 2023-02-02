package buscompany.controller;

import buscompany.dto.request.PlaceDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.dto.response.PlaceDtoResponse;
import buscompany.exception.GlobalErrorHandler;
import buscompany.utils.AppPropertiesUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PlacesControllerTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    private AppPropertiesUtils appPropertiesUtils;

    @Autowired
    public PlacesControllerTest(AppPropertiesUtils appPropertiesUtils) {
        this.appPropertiesUtils = appPropertiesUtils;
    }

    @Test
    public void testGetFreePlaces(){
        addBus("first", 5);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        int orderID = prepareChoosePlace(clientSessionID).getBody().getId();

        ResponseEntity<List<Integer>> responseEntity = template.exchange(
                "http://localhost:8080/api/places/{orderID}", HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Integer>>() {}, orderID);

        List<Integer> freePlaces = responseEntity.getBody();

        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(5, freePlaces.size())
        );
    }

    @Test
    public void testChoosePlace(){
        addBus("first", 5);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        OrderDtoResponse orderDtoResponse = prepareChoosePlace(clientSessionID).getBody();
        int orderID = orderDtoResponse.getId();

        PlaceDtoRequest placeDtoRequest = new PlaceDtoRequest(orderID, "lastName", "firstName", "passport", 5);

        ResponseEntity<PlaceDtoResponse> responseEntity = template.postForEntity(
                "http://localhost:8080/api/places", new HttpEntity<>(placeDtoRequest, headers), PlaceDtoResponse.class);

        PlaceDtoResponse place = responseEntity.getBody();

        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(5, place.getPlace()),
                () -> assertEquals("firstName", place.getFirstName()) ,
                () -> assertEquals("lastName", place.getLastName()),
                () -> assertEquals("passport", place.getPassport()),
                () -> assertEquals("Билет " + orderDtoResponse.getTripID() + "_" + place.getPlace(), place.getTicket())
        );
    }

    @Test
    public void testChoseNonExistentPlace(){
        addBus("first", 5);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        OrderDtoResponse orderDtoResponse = prepareChoosePlace(clientSessionID).getBody();
        int orderID = orderDtoResponse.getId();

        PlaceDtoRequest placeDtoRequest = new PlaceDtoRequest(orderID, "lastName", "firstName", "passport", 32);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity(
                    "http://localhost:8080/api/places", new HttpEntity<>(placeDtoRequest, headers), PlaceDtoResponse.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You entered wrong place. 0 < place <= 5", errorResponse.getMessage())
        );
    }

    @Test
    public void testChoosePlaceWrongPassport(){
        addBus("first", 5);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        OrderDtoResponse orderDtoResponse = prepareChoosePlace(clientSessionID).getBody();
        int orderID = orderDtoResponse.getId();

        PlaceDtoRequest placeDtoRequest = new PlaceDtoRequest(orderID, "lastName", "firstName", "wrong_passport", 5);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity(
                    "http://localhost:8080/api/places", new HttpEntity<>(placeDtoRequest, headers), PlaceDtoResponse.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Passenger with this passport not exist.", errorResponse.getMessage())
        );
    }

    @Test
    public void testChoosePlaceNotValidSessionID() throws InterruptedException {
        addBus("first", 5);
        int prevUserIdleTimeout = appPropertiesUtils.getUserIdleTimeout();

        appPropertiesUtils.setUserIdleTimeout(4);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        OrderDtoResponse orderDtoResponse = prepareChoosePlace(clientSessionID).getBody();
        int orderID = orderDtoResponse.getId();

        PlaceDtoRequest placeDtoRequest = new PlaceDtoRequest(orderID, "lastName", "firstName", "passport", 3);

        Thread.sleep(5000);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity(
                    "http://localhost:8080/api/places", new HttpEntity<>(placeDtoRequest, headers), PlaceDtoResponse.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Session is not valid", errorResponse.getMessage())
        );

        appPropertiesUtils.setUserIdleTimeout(prevUserIdleTimeout);

    }

    @Test
    public void testChoosePlaceByAdmin() {
        addBus("first", 5);

        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminSessionID);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        OrderDtoResponse orderDtoResponse = prepareChoosePlace(clientSessionID).getBody();
        int orderID = orderDtoResponse.getId();

        PlaceDtoRequest placeDtoRequest = new PlaceDtoRequest(orderID, "lastName", "firstName", "passport", 3);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity(
                    "http://localhost:8080/api/places", new HttpEntity<>(placeDtoRequest, adminHeaders), PlaceDtoResponse.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You have no right for this operation.", errorResponse.getMessage())
        );
    }

    @Test
    public void testChoosePlaceOccupiedByAnotherPassenger(){
        addBus("first", 5);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        OrderDtoResponse orderDtoResponse = prepareChoosePlace(clientSessionID).getBody();
        int orderID = orderDtoResponse.getId();

        PlaceDtoRequest placeDtoRequest = new PlaceDtoRequest(orderID, "lastName", "firstName", "passport", 3);

        template.postForEntity(
                "http://localhost:8080/api/places", new HttpEntity<>(placeDtoRequest, headers), PlaceDtoResponse.class);

        PlaceDtoRequest secondPlaceDtoRequest = new PlaceDtoRequest(orderID, "lastName1", "firstName1", "passport1", 3);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity(
                    "http://localhost:8080/api/places", new HttpEntity<>(secondPlaceDtoRequest, headers), PlaceDtoResponse.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("This place is occupied.", errorResponse.getMessage())
        );
    }

}