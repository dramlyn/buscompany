package buscompany.controller;

import buscompany.dto.response.BusDtoResponse;
import buscompany.service.BusService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@AllArgsConstructor
public class BusController {

    private BusService busService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BusDtoResponse> getAllBusBrands(@CookieValue("JAVASESSIONID") String sessionId){
        return busService.getAllBusBrands(sessionId);
    }

}
