package buscompany.controller;

import buscompany.dto.request.PlaceDtoRequest;
import buscompany.dto.response.PlaceDtoResponse;
import buscompany.service.PlacesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@AllArgsConstructor
public class PlacesController {

    private PlacesService placesService;

    @GetMapping(value = "/{orderID}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Integer> getFreePlaces(@CookieValue("JAVASESSIONID") String sessionID, @PathVariable("orderID") int orderID){
        return placesService.getFreePlaces(sessionID, orderID);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaceDtoResponse choosePlace(@RequestBody PlaceDtoRequest placeDtoRequest, @CookieValue("JAVASESSIONID") String sessionID){
        return placesService.choosePlace(placeDtoRequest, sessionID);
    }



}
