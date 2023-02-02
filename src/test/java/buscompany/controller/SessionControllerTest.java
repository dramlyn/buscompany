package buscompany.controller;

import buscompany.dto.request.LoginUserDtoRequest;
import buscompany.dto.request.RegisterAdminDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.exception.GlobalErrorHandler;
import buscompany.utils.AppPropertiesUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SessionControllerTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    private AppPropertiesUtils appPropertiesUtils;

    @Autowired
    public SessionControllerTest(AppPropertiesUtils appPropertiesUtils) {
        this.appPropertiesUtils = appPropertiesUtils;
    }

    @Test
    public void testLoginAndLogoutUser() {
        HttpEntity<RegisterAdminDtoRequest> request = new HttpEntity<>
                (new RegisterAdminDtoRequest("Петр", "Петров", "Петрович", "loginn", "password", "pos1"));


        ResponseEntity<AdminDtoResponse> adminResponseEntity = template
                .postForEntity("http://localhost:8080/api/admins", request, AdminDtoResponse.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", adminResponseEntity.getHeaders().get("Set-Cookie").get(0));

        template.exchange("http://localhost:8080/api/sessions", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        LoginUserDtoRequest loginUserDtoRequest = new LoginUserDtoRequest("loginn", "password");

        ResponseEntity<AdminDtoResponse> loginResponseEntity =
                template.postForEntity("http://localhost:8080/api/sessions", loginUserDtoRequest, AdminDtoResponse.class);

        AdminDtoResponse loginResponse = loginResponseEntity.getBody();

        assertAll(
                () -> assertEquals(200, loginResponseEntity.getStatusCodeValue()),
                () -> assertEquals(adminResponseEntity.getBody(), loginResponse),
                () -> assertFalse(loginResponseEntity.getHeaders().get("Set-Cookie").isEmpty())
        );
    }

    @Test
    public void testLogoutWrongSessionID() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JAVASESSIONID=wrong_cookie");
        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange("http://localhost:8080/api/sessions", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        });
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("This sessionID doesn't exist.", errorResponse.getMessage())
        );

    }

    @Test
    public void testLogoutExpiredSessionID() throws InterruptedException {
        int prevUserTimeout = appPropertiesUtils.getUserIdleTimeout();

        appPropertiesUtils.setUserIdleTimeout(4);

        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        Thread.sleep(5000);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange("http://localhost:8080/api/sessions", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("Session is not valid", errorResponse.getMessage())
        );

        appPropertiesUtils.setUserIdleTimeout(prevUserTimeout);
    }


}