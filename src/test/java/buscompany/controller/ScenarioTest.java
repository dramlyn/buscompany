package buscompany.controller;


import buscompany.dto.request.OrderDtoRequest;
import buscompany.dto.request.PassengerDtoRequest;
import buscompany.dto.request.PlaceDtoRequest;
import buscompany.dto.request.ScheduleDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.dto.response.PlaceDtoResponse;
import buscompany.dto.response.TripDtoResponse;
import buscompany.exception.GlobalErrorHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ScenarioTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();


    //добавить админа и клиента. добавить трип с автобусом, вместительностью 5 человек.
    //подтвердить его. создать ордер с двумя людьми. еще с тремя. и еще с одним(здесь ошибка).
    //занять места для первого ордера. занять места для второго, но последнему попробовать занять занятое место.
    //потом занять правильное место. удалить первый ордер. проверить свободные места.
    @Test
    public void testScenario() {
        addBus("first", 5);

        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminSessionID);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.add("Cookie", clientSessionID);

        ResponseEntity<TripDtoResponse> trip = addTrip(adminHeaders, new ScheduleDtoRequest("2022-10-10", "2022-10-15", "odd"), null, "first");

        HttpEntity<Void> headersEntity = new HttpEntity<>(adminHeaders);
        int tripID = trip.getBody().getId();

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, headersEntity, TripDtoResponse.class, tripID);

        List<PassengerDtoRequest> firstPassengerList = new ArrayList<>();
        firstPassengerList.add(new PassengerDtoRequest("name", "lastname", "passport1"));
        firstPassengerList.add(new PassengerDtoRequest("name", "lastname", "passport2"));

        HttpEntity<OrderDtoRequest> firstOrderRequest = new HttpEntity<>(
                new OrderDtoRequest(tripID, "2022-10-11", firstPassengerList), clientHeaders);

        ResponseEntity<OrderDtoResponse> firstOrderResponse =
                template.postForEntity("http://localhost:8080/api/orders", firstOrderRequest, OrderDtoResponse.class);

        //-----------------------------------


        List<PassengerDtoRequest> secondPassengerList = new ArrayList<>();
        secondPassengerList.add(new PassengerDtoRequest("name", "lastname", "passport3"));
        secondPassengerList.add(new PassengerDtoRequest("name", "lastname", "passport4"));
        secondPassengerList.add(new PassengerDtoRequest("name", "lastname", "passport5"));

        HttpEntity<OrderDtoRequest> secondOrderRequest = new HttpEntity<>(
                new OrderDtoRequest(tripID, "2022-10-11", secondPassengerList), clientHeaders);

        ResponseEntity<OrderDtoResponse> secondOrderResponse =
                template.postForEntity("http://localhost:8080/api/orders", secondOrderRequest, OrderDtoResponse.class);


        //----------------------------------

        List<PassengerDtoRequest> thirdPassengerList = new ArrayList<>();
        thirdPassengerList.add(new PassengerDtoRequest("name", "lastname", "passport6"));

        HttpEntity<OrderDtoRequest> thirdOrderRequest = new HttpEntity<>(
                new OrderDtoRequest(tripID, "2022-10-11", thirdPassengerList), clientHeaders);


        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity("http://localhost:8080/api/orders", thirdOrderRequest, OrderDtoResponse.class);
        });

        GlobalErrorHandler.ErrorResponse addOrderErrorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Not enough space for passengers.", addOrderErrorResponse.getMessage())
        );

        //----------------------------------------

        int firstOrderID = firstOrderResponse.getBody().getId();

        PlaceDtoRequest firstPlace = new PlaceDtoRequest(firstOrderID, "lastname", "name", "passport1", 1);
        template.postForEntity("http://localhost:8080/api/places",
                new HttpEntity<>(firstPlace, clientHeaders), PlaceDtoResponse.class);

        PlaceDtoRequest secondPlace = new PlaceDtoRequest(firstOrderID, "lastname", "name", "passport2", 2);
        template.postForEntity("http://localhost:8080/api/places",
                new HttpEntity<>(secondPlace, clientHeaders), PlaceDtoResponse.class);


        int secondOrderID = secondOrderResponse.getBody().getId();

        PlaceDtoRequest thirdPlace = new PlaceDtoRequest(secondOrderID, "lastname", "name", "passport3", 3);
        template.postForEntity("http://localhost:8080/api/places",
                new HttpEntity<>(thirdPlace, clientHeaders), PlaceDtoResponse.class);

        PlaceDtoRequest fourthPlace = new PlaceDtoRequest(secondOrderID, "lastname", "name", "passport4", 4);
        template.postForEntity("http://localhost:8080/api/places",
                new HttpEntity<>(fourthPlace, clientHeaders), PlaceDtoResponse.class);

        PlaceDtoRequest tryTakeThirdPlace = new PlaceDtoRequest(secondOrderID, "lastname", "name", "passport5", 3);
        HttpClientErrorException secondExc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity("http://localhost:8080/api/places",
                    new HttpEntity<>(tryTakeThirdPlace, clientHeaders), PlaceDtoResponse.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(secondExc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("This place is occupied.", errorResponse.getMessage())
        );

        PlaceDtoRequest fifthPlace = new PlaceDtoRequest(secondOrderID, "lastname", "name", "passport5", 5);
        template.postForEntity("http://localhost:8080/api/places",
                new HttpEntity<>(fifthPlace, clientHeaders), PlaceDtoResponse.class);

        template.exchange("http://localhost:8080/api/orders/{orderID}", HttpMethod.DELETE, new HttpEntity<>(clientHeaders), Void.class, firstOrderID);

        ResponseEntity<List<Integer>> freeSeatsResponse = template.exchange("http://localhost:8080/api/places/{orderID}", HttpMethod.GET,
                new HttpEntity<>(clientHeaders), new ParameterizedTypeReference<List<Integer>>() {}, secondOrderID);

        List<Integer> freeSeats = freeSeatsResponse.getBody();

        assertAll(
                () -> assertTrue(freeSeats.contains(1)),
                () -> assertTrue(freeSeats.contains(2))
        );



    }
}
