package buscompany.controller;

import buscompany.dto.request.RegisterClientDtoRequest;
import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.dto.response.GetAllClientsDtoResponse;
import buscompany.dto.response.UpdateClientDtoResponse;
import buscompany.service.ClientService;
import buscompany.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
public class ClientController {

    private ClientService clientService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientDtoResponse registerClient(@RequestBody @Valid RegisterClientDtoRequest client, HttpServletResponse servletResponse){
        return clientService.registerClient(client, servletResponse);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAllClientsDtoResponse getAllClients(@CookieValue("JAVASESSIONID") String sessionID){
        return clientService.getAllClients(sessionID);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UpdateClientDtoResponse updateClient(@RequestBody @Valid UpdateClientDtoRequest updateClient, @CookieValue("JAVASESSIONID") String sessionID){
        return clientService.updateClient(updateClient, sessionID);
    }

}
