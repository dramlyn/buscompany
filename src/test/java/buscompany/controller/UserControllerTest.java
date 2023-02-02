package buscompany.controller;

import buscompany.dto.request.RegisterClientDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.exception.GlobalErrorHandler;
import buscompany.model.UserType;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest extends TestBase {

    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    @Test
    public void testLeaveUser(){
        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

       assertEquals(200, template.exchange("http://localhost:8080/api/accounts",
               HttpMethod.DELETE, new HttpEntity<>(headers), Void.class).getStatusCodeValue());

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
           template.exchange("http://localhost:8080/api/accounts",HttpMethod.GET, new HttpEntity<>(headers), Void.class);
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("This sessionID doesn't exist.", errorResponse.getMessage())
        );
    }

    @Test
    public void testGetUserBySessionID(){
        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        ResponseEntity<AdminDtoResponse> adminResponse = template.exchange("http://localhost:8080/api/accounts",HttpMethod.GET, new HttpEntity<>(headers), AdminDtoResponse.class);

        AdminDtoResponse admin = adminResponse.getBody();

        assertAll(
                () -> assertEquals(200, adminResponse.getStatusCodeValue()),
                () -> assertEquals("Петр", admin.getFirstName()),
                () -> assertEquals("Петров", admin.getLastName()),
                () -> assertEquals("Петрович", admin.getPatronymic()),
                () -> assertEquals("pos1", admin.getPosition()),
                () -> assertEquals(UserType.ADMIN, admin.getUserType()));
    }

}