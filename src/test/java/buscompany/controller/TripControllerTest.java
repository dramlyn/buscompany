package buscompany.controller;

import buscompany.dto.request.ScheduleDtoRequest;
import buscompany.dto.request.TripDtoRequest;
import buscompany.dto.request.UpdateTripDtoRequest;
import buscompany.dto.response.BusDtoResponse;
import buscompany.dto.response.ScheduleDtoResponse;
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
class TripControllerTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    @Test
    public void testAddTrip(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        ResponseEntity<TripDtoResponse> responseEntity = addTrip
                (headers, new ScheduleDtoRequest("2022-12-12","2022-12-16","daily"),null,"first");

        TripDtoResponse responseBody = responseEntity.getBody();
        ScheduleDtoResponse schedule = responseBody.getSchedule();
        BusDtoResponse bus = responseBody.getBus();
        LinkedList<LocalDate> dates = responseBody.getDates();
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(5, dates.size()),
                () -> assertTrue(dates.containsAll(Arrays.asList(LocalDate.parse("2022-12-12"),
                        LocalDate.parse("2022-12-13"),LocalDate.parse("2022-12-14"),LocalDate.parse("2022-12-15"),LocalDate.parse("2022-12-16")))),
                () -> assertEquals(2, responseBody.getDuration()),
                () -> assertEquals("fromStation",responseBody.getFromStation()),
                () -> assertEquals("toStation", responseBody.getToStation()),
                () -> assertEquals(100, responseBody.getPrice()),
                () -> assertEquals(LocalTime.parse("15:30"),responseBody.getStart()),
                () -> assertEquals("2022-12-12",schedule.getFromDate()),
                () -> assertEquals("2022-12-16", schedule.getToDate()),
                () -> assertEquals("daily", schedule.getPeriod()),
                () -> assertEquals("first", bus.getBusName()),
                () -> assertEquals(5, bus.getPlaceCount())
        );
    }

    @Test
    public void testUpdateTrip(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        ResponseEntity<TripDtoResponse> addTrip= addTrip(headers, null, dates, "first");

        HttpEntity<UpdateTripDtoRequest> updateEntity = new HttpEntity<>
                (new UpdateTripDtoRequest("first", "update_fromStation",
                        "update_toStation", "14:20", 6, 165,
                        new ScheduleDtoRequest("2022-08-21","2022-08-26","even"), null), headers);

        ResponseEntity<TripDtoResponse> responseEntity = template.exchange("http://localhost:8080/api/trips/{tripID}",
                HttpMethod.PUT, updateEntity, TripDtoResponse.class, addTrip.getBody().getId());

        TripDtoResponse responseBody = responseEntity.getBody();
        ScheduleDtoResponse schedule = responseBody.getSchedule();
        BusDtoResponse bus = responseBody.getBus();
        LinkedList<LocalDate> responseDates = responseBody.getDates();

        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(3, responseDates.size()),
                () -> assertTrue(responseDates.containsAll(Arrays.asList(LocalDate.parse("2022-08-22"),
                        LocalDate.parse("2022-08-24"),LocalDate.parse("2022-08-26")))),
                () -> assertEquals(6, responseBody.getDuration()),
                () -> assertEquals("update_fromStation",responseBody.getFromStation()),
                () -> assertEquals("update_toStation", responseBody.getToStation()),
                () -> assertEquals(165, responseBody.getPrice()),
                () -> assertEquals(LocalTime.parse("14:20"),responseBody.getStart()),
                () -> assertEquals("2022-08-21",schedule.getFromDate()),
                () -> assertEquals("2022-08-26", schedule.getToDate()),
                () -> assertEquals("even", schedule.getPeriod()),
                () -> assertEquals("first", bus.getBusName()),
                () -> assertEquals(5, bus.getPlaceCount())
        );
    }

    @Test
    public void testDeleteTrip(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

       ResponseEntity<TripDtoResponse> addTripRespoonse = addTrip(headers, null, dates, "first");

       HttpEntity<Void> headersEntity = new HttpEntity<>(headers);

       int tripID = addTripRespoonse.getBody().getId();

       assertEquals(200, template.exchange("http://localhost:8080/api/trips/{tripID}",
               HttpMethod.DELETE, headersEntity, Void.class, tripID).getStatusCodeValue());

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange("http://localhost:8080/api/trips/{tripID}",HttpMethod.GET, headersEntity, Void.class, tripID);
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals("You entered wrong tripID.", errorResponse.getMessage()),
                () -> assertEquals(400, exc.getStatusCode().value())
        );
    }

    @Test
    public void testApproveTrip(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        ResponseEntity<TripDtoResponse> addTripResponse = addTrip(headers, null, dates, "first");

        HttpEntity<Void> headersEntity = new HttpEntity<>(headers);

        int tripID = addTripResponse.getBody().getId();

        ResponseEntity<TripDtoResponse> tripResponseEntity = template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, headersEntity, TripDtoResponse.class, tripID);

        assertAll(
                () -> assertEquals(200, tripResponseEntity.getStatusCodeValue()),
                () -> assertTrue(tripResponseEntity.getBody().isApproved())
        );
    }

    @Test
    public void testGetTripList(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        addTrip(headers, null, dates, "first");
        addTrip(headers, null, dates, "first");

        HttpEntity<Void> headersEntity = new HttpEntity<>(headers);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("busName", "first");


        ResponseEntity<List<TripDtoResponse>> tripListResponseEntity =
                template.exchange("http://localhost:8080/api/trips", HttpMethod.GET, headersEntity, new ParameterizedTypeReference<List<TripDtoResponse>>() {}, queryParams);

        List<TripDtoResponse> list = tripListResponseEntity.getBody();

        assertAll(
                () -> assertEquals(200, tripListResponseEntity.getStatusCodeValue()),
                () -> assertEquals(2, list.size()),
                () -> assertTrue(list.stream().allMatch(trip -> trip.getBus().getBusName().equals("first")))
        );
    }

    @Test
    public void testAddTripNullScheduleAndDates(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
           addTrip(headers, null, null, "first");
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Schedule or dates must be null.", errorResponse.getMessage())
        );
    }

    @Test
    public void testAddTripNotNullScheduleAndDates(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        ScheduleDtoRequest scheduleDtoRequest = new ScheduleDtoRequest("2022-08-21","2022-08-26","even");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            addTrip(headers, scheduleDtoRequest, dates, "first");
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Schedule or dates must be null.", errorResponse.getMessage())
        );
    }

    @Test
    public void testAddTripWrongDates(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-13-15");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            addTrip(headers, null, dates, "first");
        });

        GlobalErrorHandler.ValidationExcResponse validationExcResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ValidationExcResponse.class);
        List<GlobalErrorHandler.ErrorResponse> errorResponses = validationExcResponse.getErrorResponses();

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals(1, errorResponses.size()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("YYYY-MM(1-12)-DD(1-31)")).count())
        );
    }

    @Test
    public void testAddTripWrongMonthDate(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-09-31");
        dates.add("2022-12-15");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            addTrip(headers, null, dates, "first");
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You entered invalid date", errorResponse.getMessage())
        );
    }

    @Test
    public void testUpdateApprovedTrip(){
        addBus("first", 5);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        int tripID = addTrip(headers, null, dates, "first").getBody().getId();

        HttpEntity<Void> headersEntity = new HttpEntity<>(headers);

        template.exchange("http://localhost:8080/api/trips/{tripID}/approve",
                HttpMethod.PUT, headersEntity, TripDtoResponse.class, tripID);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            updateTrip(tripID, headers);
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You can't update trip. This trip is approved.", errorResponse.getMessage())
        );
    }

    @Test
    public void testAddTripByClient(){
        addBus("first", 5);

        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        SortedSet<String> dates = new TreeSet<>();
        dates.add("2022-11-11");
        dates.add("2022-11-15");
        dates.add("2022-11-17");
        dates.add("2022-11-12");
        dates.add("2022-11-10");

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            addTrip(headers, null, dates, "first");
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You have no right for this operation.", errorResponse.getMessage())
        );
    }



}