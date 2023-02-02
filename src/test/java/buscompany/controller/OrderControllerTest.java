package buscompany.controller;

import buscompany.dto.request.OrderDtoRequest;
import buscompany.dto.request.PassengerDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OrderControllerTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    @Test
    public void testInsertOrder(){
        addBus("first", 5);

        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminSessionID);
        HttpEntity<Void> adminHeadersEntity = new HttpEntity<>(adminHeaders);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.add("Cookie", clientSessionID);


        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        int tripID = addTrip(adminHeaders, null, dates, "first").getBody().getId();

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, adminHeadersEntity, TripDtoResponse.class, tripID);

        ResponseEntity<OrderDtoResponse> orderEntity =
                addOrder(clientHeaders, tripID, "2022-11-12");

        OrderDtoResponse orderDtoResponse = orderEntity.getBody();

        assertAll(
                () -> assertEquals(200, orderEntity.getStatusCodeValue()),
                () -> assertEquals(2 ,orderDtoResponse.getPassengers().size()),
                () -> assertEquals("first", orderDtoResponse.getBusName()),
                () -> assertEquals("fromStation", orderDtoResponse.getFromStation()),
                () -> assertEquals("toStation", orderDtoResponse.getToStation()),
                () -> assertEquals(100, orderDtoResponse.getPrice()),
                () -> assertEquals(200, orderDtoResponse.getTotalPrice()),
                () -> assertEquals(tripID, orderDtoResponse.getTripID()),
                () -> assertEquals(LocalTime.parse("15:30"), orderDtoResponse.getStart()),
                () -> assertEquals(LocalDate.parse("2022-11-12"), orderDtoResponse.getDate()),
                () -> assertEquals(2, orderDtoResponse.getDuration())
        );
    }

    @Test
    public void testGetOrderList(){
        addBus("first", 5);

        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminSessionID);
        HttpEntity<Void> adminHeadersEntity = new HttpEntity<>(adminHeaders);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.add("Cookie", clientSessionID);


        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        int firstTripID = addTrip(adminHeaders, null, dates, "first").getBody().getId();
        int secondTripID = addTrip(adminHeaders, null, dates, "first").getBody().getId();
        int thirdTripID = addTrip(adminHeaders, null, dates, "first").getBody().getId();

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, adminHeadersEntity, TripDtoResponse.class, firstTripID);

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, adminHeadersEntity, TripDtoResponse.class, secondTripID);

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, adminHeadersEntity, TripDtoResponse.class, thirdTripID);


        addOrder(clientHeaders, firstTripID, "2022-11-11");
        addOrder(clientHeaders, secondTripID, "2022-11-11");
        addOrder(clientHeaders, thirdTripID, "2022-11-10");

        String fromStation = "fromStation";
        String fromDate = "2022-11-11";

        ResponseEntity<List<OrderDtoResponse>> orderListResponseEntity =
                template.exchange("http://localhost:8080/api/orders?fromStation={fromStation}&fromDate={fromDate}", HttpMethod.GET, adminHeadersEntity, new ParameterizedTypeReference<List<OrderDtoResponse>>() {}, fromStation, fromDate);

        List<OrderDtoResponse> orderList = orderListResponseEntity.getBody();

        assertAll(
                () -> assertEquals(200, orderListResponseEntity.getStatusCodeValue()),
                () -> assertEquals(2,orderList.size()),
                () -> assertTrue(orderList.stream().allMatch(order -> order.getFromStation().equals("fromStation"))),
                () -> assertTrue(orderList.stream().allMatch(order -> order.getDate().equals(LocalDate.parse("2022-11-11"))))
        );
    }

    @Test
    public void testCancelOrder(){
        addBus("first", 5);

        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminSessionID);
        HttpEntity<Void> adminHeadersEntity = new HttpEntity<>(adminHeaders);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.add("Cookie", clientSessionID);


        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        int tripID = addTrip(adminHeaders, null, dates, "first").getBody().getId();

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, adminHeadersEntity, TripDtoResponse.class, tripID);

        ResponseEntity<OrderDtoResponse> orderEntity =
                addOrder(clientHeaders, tripID, "2022-11-12");

        int orderID = orderEntity.getBody().getId();

        assertEquals(200,template.exchange("http://localhost:8080/api/orders/{orderID}",
                HttpMethod.DELETE, new HttpEntity<>(clientHeaders), Void.class, orderID).getStatusCodeValue());

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            assertEquals(200,template.exchange("http://localhost:8080/api/orders/{orderID}",
                    HttpMethod.DELETE, new HttpEntity<>(clientHeaders), Void.class, orderID).getStatusCodeValue());
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You entered wrong orderID.", errorResponse.getMessage())
        );
    }

    @Test
    public void testAddWrongOrder(){
        addBus("first", 5);
        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        int tripID = prepareAddOrder();

        List<PassengerDtoRequest> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDtoRequest(null, "", "passport"));
        passengerList.add(new PassengerDtoRequest("firstName", "lastName", "passport1"));

        OrderDtoRequest orderDtoRequest = new OrderDtoRequest(tripID, "2000-18-34", passengerList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);


        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
           template.postForEntity("http://localhost:8080/api/orders", new HttpEntity<>(orderDtoRequest, headers), OrderDtoResponse.class);
        });

        GlobalErrorHandler.ValidationExcResponse validationExcResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ValidationExcResponse.class);

        List<GlobalErrorHandler.ErrorResponse> errorList = validationExcResponse.getErrorResponses();
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals(4, errorList.size()),
                () -> assertEquals(1, errorList.stream().filter(error -> error.getMessage().equals("Can't be null.")).count()),
                () -> assertEquals(2, errorList.stream().filter(error -> error.getMessage().equals("Can't be blank.")).count()),
                () -> assertEquals(1, errorList.stream().filter(error -> error.getMessage().equals("YYYY-MM(1-12)-DD(1-31)")).count())
        );
    }


    @Test
    public void testAddOrderNotEnoughPlace(){
        addBus("first", 2);

        String clientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientSessionID);

        int tripID = prepareAddOrder();

        List<PassengerDtoRequest> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDtoRequest("firstName", "lastName", "passport"));
        passengerList.add(new PassengerDtoRequest("firstName1", "lastName1", "passport1"));
        passengerList.add(new PassengerDtoRequest("firstName2", "lastName2", "passport2"));

        OrderDtoRequest orderDtoRequest = new OrderDtoRequest(tripID, "2022-11-15", passengerList);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity("http://localhost:8080/api/orders",new HttpEntity<>(orderDtoRequest, headers), OrderDtoResponse.class);
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        System.out.println(errorResponse.getMessage());
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Not enough space for passengers.", errorResponse.getMessage())
        );
    }

    @Test
    public void testOrderIDBelonging(){
        addBus("first", 2);

        String rightClientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders rightHeaders = new HttpHeaders();
        rightHeaders.add("Cookie", rightClientSessionID);

        String wrongClientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders wrongHeaders = new HttpHeaders();
        wrongHeaders.add("Cookie", wrongClientSessionID);

        int tripID = prepareAddOrder();

        ResponseEntity<OrderDtoResponse> response = addOrder(rightHeaders, tripID, "2022-11-15");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange("http://localhost:8080/api/orders/{orderID}",
                    HttpMethod.DELETE, new HttpEntity<>(wrongHeaders), Void.class, response.getBody().getId());
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You entered not yours orderID.", errorResponse.getMessage())
        );

    }

    @Test
    public void testAddOrderByAdmin(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        int tripID = prepareAddOrder();

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            addOrder(headers, tripID, "2022-11-15");
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You have no right for this operation.", errorResponse.getMessage())
        );

    }

    @Test
    public void testGetOrderListByClient(){
        addBus("first", 5);

        int tripID = prepareAddOrder();

        String firstClientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders firstClientHeaders = new HttpHeaders();
        firstClientHeaders.add("Cookie", firstClientSessionID);

        String secondClientSessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders secondClientHeaders = new HttpHeaders();
        secondClientHeaders.add("Cookie", secondClientSessionID);

        addOrder(firstClientHeaders, tripID, "2022-11-15");

        addOrder(secondClientHeaders, tripID, "2022-11-11");


        ResponseEntity<List<OrderDtoResponse>> responseEntity = template.exchange(
                "http://localhost:8080/api/orders", HttpMethod.GET, new HttpEntity<>(secondClientHeaders),
                new ParameterizedTypeReference<List<OrderDtoResponse>>() {});

        List<OrderDtoResponse> orderList = responseEntity.getBody();

        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(1, orderList.size()),
                () -> assertEquals(LocalDate.parse("2022-11-11"), orderList.get(0).getDate())
        );
    }

    @Test
    public void testAddOrderWrongDate(){
        addBus("first", 5);

        int tripID = prepareAddOrder();

        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            addOrder(headers, tripID, "2022-11-29");
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("In your order you entered wrong date.", errorResponse.getMessage())
        );
    }

}