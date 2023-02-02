package buscompany.controller;


import buscompany.dto.request.RegisterAdminDtoRequest;
import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminControllerTest extends TestBase {


    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();


    @Test
    public void testRegisterAdmin() {
        ResponseEntity<AdminDtoResponse> response = registerAdmin();
        AdminDtoResponse admin = response.getBody();
        assertAll(
                () -> assertTrue(response.getHeaders().containsKey("Set-Cookie")),
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("Петр", admin.getFirstName()),
                () -> assertEquals("Петров", admin.getLastName()),
                () -> assertEquals("Петрович", admin.getPatronymic()),
                () -> assertEquals("pos1", admin.getPosition()),
                () -> assertEquals(UserType.ADMIN, admin.getUserType()));
    }

    @Test
    public void testUpdateAdmin() {
        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        HttpEntity<UpdateAdminDtoRequest> updateRequest = new HttpEntity<>
                (new UpdateAdminDtoRequest("Пётр", "Иванов", "Викторович", "password", "new_password", "new_position"), headers);

        ResponseEntity<AdminDtoResponse> response = template.exchange("http://localhost:8080/api/admins", HttpMethod.PUT, updateRequest, AdminDtoResponse.class);
        AdminDtoResponse admin = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("Пётр", admin.getFirstName()),
                () -> assertEquals("Иванов", admin.getLastName()),
                () -> assertEquals("Викторович", admin.getPatronymic()),
                () -> assertEquals("new_position", admin.getPosition()),
                () -> assertEquals(UserType.ADMIN, admin.getUserType()));
    }

    @Test
    public void testRegisterAdminWrongName() {
        HttpEntity<RegisterAdminDtoRequest> registerRequest = new HttpEntity<>(new RegisterAdminDtoRequest("Petr", "PetrovPetrovPetrovPetrovPetrovPetrov", "Petrovich", "login3", "password2", "pos2"));

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> template.postForEntity("http://localhost:8080/api/admins", registerRequest, AdminDtoResponse.class));

        GlobalErrorHandler.ValidationExcResponse responses = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ValidationExcResponse.class);
        List<GlobalErrorHandler.ErrorResponse> errorResponses = responses.getErrorResponses();
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals(4, errorResponses.size()),
                () -> assertEquals(3, errorResponses.stream().filter(item -> item.getMessage().equals("Only rus symbols, minus and space character.")).count()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("You entered wrong name")).count())
        );
    }

    @Test
    public void testRegisterAdminDuplicateLogin() {
        HttpEntity<RegisterAdminDtoRequest> request = new HttpEntity<>
                (new RegisterAdminDtoRequest("Петр", "Петров", "Петрович", "login4", "password4", "pos4"));
        template.postForEntity("http://localhost:8080/api/admins", request, AdminDtoResponse.class);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () ->
                template.postForEntity("http://localhost:8080/api/admins", request, AdminDtoResponse.class)
        );
        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(errorResponse.getMessage().contains("Duplicate login")));
    }

    @Test
    public void testUpdateAdminWrongSessionID() {
        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID + "1");

        HttpEntity<UpdateAdminDtoRequest> updateRequest = new HttpEntity<>
                (new UpdateAdminDtoRequest("Пётр", "Иванов", "Викторович", "password2", "new_password", "new_position"), headers);

        HttpClientErrorException exc =  assertThrows(HttpClientErrorException.class, () ->
                template.exchange("http://localhost:8080/api/admins", HttpMethod.PUT, updateRequest, AdminDtoResponse.class));

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);
        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertTrue(errorResponse.getMessage().contains("This sessionID doesn't exist.")));
    }

}