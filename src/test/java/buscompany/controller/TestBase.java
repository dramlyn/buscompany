package buscompany.controller;

import buscompany.dto.request.*;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.dto.response.TripDtoResponse;
import buscompany.utils.MyBatisUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class TestBase {

    private int counter = 0;

    private RestTemplate template = new RestTemplate();

    private final Gson gson = new Gson();

    @BeforeAll
    static void connection() {
        MyBatisUtils.initSqlSessionFactory();
    }

    @BeforeEach
    public void clearDataBase() {
        new RestTemplate().postForObject("http://localhost:8080/api/debug/clear", null, Void.class);
    }

    public RestTemplate getTemplate() {
        return template;
    }

    public Gson getGson() {
        return gson;
    }

    public int getCounter() {
        return counter;
    }

    public ResponseEntity<ClientDtoResponse> registerClient(){
        HttpEntity<RegisterClientDtoRequest> request = new HttpEntity<>
                (new RegisterClientDtoRequest("Петр", "Петров", "Петрович", "login" + counter, "password", "petr@mail.ru", "+7-777-777-77-77"));
        counter++;
        return template
                .postForEntity("http://localhost:8080/api/clients", request, ClientDtoResponse.class);
    }

    public ResponseEntity<AdminDtoResponse> registerAdmin(){
        HttpEntity<RegisterAdminDtoRequest> request = new HttpEntity<>
                (new RegisterAdminDtoRequest("Петр", "Петров", "Петрович", "login" + counter, "password", "pos1"));
        counter++;
        return template
                .postForEntity("http://localhost:8080/api/admins", request, AdminDtoResponse.class);
    }

    public void addBus(String busName, int placeCount){
        HttpEntity<BusDtoRequest> request = new HttpEntity<>(new BusDtoRequest(busName, placeCount));
        template.postForEntity("http://localhost:8080/api/buses", request, Void.class);
    }

    public ResponseEntity<TripDtoResponse> addTrip(HttpHeaders headers, ScheduleDtoRequest scheduleDtoRequest, SortedSet<String> dates, String busName){
        HttpEntity<TripDtoRequest> request = new HttpEntity<>
                (new TripDtoRequest(busName, "fromStation", "toStation", "15:30", 2, 100, scheduleDtoRequest, dates), headers);
        return template.postForEntity("http://localhost:8080/api/trips", request, TripDtoResponse.class);
    }

    public ResponseEntity<TripDtoResponse> updateTrip(int tripID, HttpHeaders headers){
        HttpEntity<UpdateTripDtoRequest> updateEntity = new HttpEntity<>
                (new UpdateTripDtoRequest("first", "update_fromStation",
                        "update_toStation", "14:20", 6, 165,
                        new ScheduleDtoRequest("2022-08-21","2022-08-26","even"), null), headers);

        return template.exchange("http://localhost:8080/api/trips/{tripID}",
                HttpMethod.PUT, updateEntity, TripDtoResponse.class, tripID);
    }

    public ResponseEntity<OrderDtoResponse> addOrder(HttpHeaders headers, int tripID, String date){
        List<PassengerDtoRequest> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDtoRequest("firstName", "lastName", "passport"));
        passengerList.add(new PassengerDtoRequest("firstName1", "lastName1", "passport1"));

        HttpEntity<OrderDtoRequest> orderRequest = new HttpEntity<>(
                new OrderDtoRequest(tripID, date, passengerList), headers);

        return template.postForEntity("http://localhost:8080/api/orders", orderRequest, OrderDtoResponse.class);
    }

    public int prepareAddOrder(){

        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminSessionID);
        HttpEntity<Void> adminHeadersEntity = new HttpEntity<>(adminHeaders);


        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        int tripID = addTrip(adminHeaders, null, dates, "first").getBody().getId();

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, adminHeadersEntity, TripDtoResponse.class, tripID);

        return tripID;
    }

    public ResponseEntity<OrderDtoResponse> prepareChoosePlace(String clientSessionID){
        String adminSessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

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

        List<PassengerDtoRequest> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDtoRequest("firstName", "lastName", "passport"));
        passengerList.add(new PassengerDtoRequest("firstName1", "lastName1", "passport1"));

        HttpEntity<OrderDtoRequest> orderRequest = new HttpEntity<>(
                new OrderDtoRequest(tripID, "2022-11-11", passengerList), clientHeaders);

        return template.postForEntity("http://localhost:8080/api/orders", orderRequest, OrderDtoResponse.class);
    }
}
