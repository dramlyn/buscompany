package buscompany.controller;

import buscompany.dto.response.UserDtoResponse;
import buscompany.service.SessionService;
import buscompany.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class UserController {
    private SessionService sessionService;

    private UserService userService;

    @DeleteMapping
    void leave(@CookieValue(value = "JAVASESSIONID") String sessionID){
        sessionService.leave(sessionID);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    UserDtoResponse getUserBySessionId(@CookieValue(value = "JAVASESSIONID") String sessionID){
        return userService.getUser(sessionID);
    }

}
