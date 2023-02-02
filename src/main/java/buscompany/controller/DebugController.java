package buscompany.controller;

import buscompany.dto.request.BusDtoRequest;
import buscompany.dto.response.AppPropertiesResponse;
import buscompany.service.DebugService;
import buscompany.utils.AppPropertiesUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class DebugController {

    private AppPropertiesUtils prop;

    private DebugService debugService;


    @GetMapping(value = "/api/settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppPropertiesResponse getProp(@CookieValue("JAVASESSIONID") String sessionID){
        return new AppPropertiesResponse(prop.getMaxNameLength(), prop.getMinPasswordLength());
    }

    @PostMapping(value = "/api/debug/clear")
    public void deleteDataBase(){
        debugService.deleteDataBase();
    }

    @PostMapping(value = "/api/buses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addBus(@RequestBody BusDtoRequest busDtoRequest){
        debugService.addBus(busDtoRequest);
    }

}
