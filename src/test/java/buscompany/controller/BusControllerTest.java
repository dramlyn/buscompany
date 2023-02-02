package buscompany.controller;

import buscompany.dto.response.BusDtoResponse;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BusControllerTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    private AppPropertiesUtils appPropertiesUtils;

    @Autowired
    public BusControllerTest(AppPropertiesUtils appPropertiesUtils) {
        this.appPropertiesUtils = appPropertiesUtils;
    }

    @Test
    public void testGetAllBusBrands(){
        addBus("first", 5);
        addBus("second", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        ResponseEntity<List<BusDtoResponse>> responseEntity = template.exchange(
                "http://localhost:8080/api/buses", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<BusDtoResponse>>() {});

        List<BusDtoResponse> busList = responseEntity.getBody();

        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(2, busList.size())
        );

    }

    @Test
    public void testGetAllBusBrandsByClient() {
        addBus("first", 5);
        addBus("second", 5);

        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    "http://localhost:8080/api/buses", HttpMethod.GET,
                    new HttpEntity<>(headers), new ParameterizedTypeReference<List<BusDtoResponse>>() {});
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You have no right for this operation.",errorResponse.getMessage())
        );
    }

    @Test
    public void testGetAllBusBrandsWithWrongSessionID(){
        addBus("first", 5);
        addBus("second", 5);

        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID + "wrong");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    "http://localhost:8080/api/buses", HttpMethod.GET,
                    new HttpEntity<>(headers), new ParameterizedTypeReference<List<BusDtoResponse>>() {});
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("This sessionID doesn't exist.",errorResponse.getMessage())
        );
    }

    @Test
    public void testGetAllBusBrandsExpiredSessionID() throws InterruptedException {
        int prevUserIdleTimeout = appPropertiesUtils.getUserIdleTimeout();
        appPropertiesUtils.setUserIdleTimeout(4);

        addBus("first", 5);
        addBus("second", 5);

        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        Thread.sleep(5000);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange(
                    "http://localhost:8080/api/buses", HttpMethod.GET,
                    new HttpEntity<>(headers), new ParameterizedTypeReference<List<BusDtoResponse>>() {});
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Session is not valid", errorResponse.getMessage())
        );

        appPropertiesUtils.setUserIdleTimeout(prevUserIdleTimeout);
    }

}