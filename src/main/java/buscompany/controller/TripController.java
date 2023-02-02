package buscompany.controller;

import buscompany.dto.request.TripDtoRequest;
import buscompany.dto.request.UpdateTripDtoRequest;
import buscompany.dto.response.TripDtoResponse;
import buscompany.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@AllArgsConstructor
public class TripController {

    private TripService tripService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse addTrip(@RequestBody @Valid TripDtoRequest tripRequest, @CookieValue("JAVASESSIONID") String sessionID){
        return tripService.addTrip(tripRequest, sessionID);
    }

    @PutMapping(value = "/{tripID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse updateTrip(@PathVariable("tripID") int tripID, @RequestBody @Valid UpdateTripDtoRequest tripDtoRequest,
                                      @CookieValue("JAVASESSIONID") String sessionID){
        return tripService.updateTrip(tripDtoRequest, tripID, sessionID);
    }

    @DeleteMapping(value = "/{tripID}")
    public void deleteTrip(@PathVariable("tripID") int tripID, @CookieValue("JAVASESSIONID") String sessionID){
        tripService.deleteTrip(tripID, sessionID);
    }

    @GetMapping(value = "/{tripID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse getTripById(@PathVariable("tripID") int tripID, @CookieValue("JAVASESSIONID") String sessionID){
        return tripService.getTripByID(tripID, sessionID);
    }

    @PutMapping(value = "/{tripID}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse approveTrip(@PathVariable("tripID") int tripID, @CookieValue("JAVASESSIONID") String sessionID){
       return tripService.approveTrip(tripID, sessionID);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripDtoResponse> getTripList(@RequestParam(value = "fromStation", required = false) String fromStation,
                                             @RequestParam(value = "toStation", required = false) String toStation,
                                             @RequestParam(value = "busName", required = false) String busName,
                                             @RequestParam(value = "fromDate", required = false) String fromDate,
                                             @RequestParam(value = "toDate", required = false) String toDate,
                                             @CookieValue("JAVASESSIONID") String sessionID)
    {
        return tripService.getTripList(fromStation, toStation, busName, fromDate, toDate, sessionID);
    }
}
