package buscompany.controller;

import buscompany.dto.request.LoginUserDtoRequest;
import buscompany.dto.response.UserDtoResponse;
import buscompany.service.SessionService;
import buscompany.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/sessions")
@AllArgsConstructor
public class SessionController {

    private SessionService sessionService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDtoResponse login(@RequestBody @Valid LoginUserDtoRequest loginRequest, HttpServletResponse httpResponse){
        return sessionService.login(loginRequest, httpResponse);
    }

    @DeleteMapping
    public void logout(@CookieValue(value = "JAVASESSIONID") String sessionId){
        sessionService.logout(sessionId);
    }





}
