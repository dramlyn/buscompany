package buscompany.controller;

import buscompany.dto.request.RegisterClientDtoRequest;
import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.dto.response.GetAllClientsDtoResponse;
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
class ClientControllerTest extends TestBase {



    private RestTemplate template = getTemplate();

    private final Gson gson = getGson();

    @Test
    public void testRegisterClient() {

        ResponseEntity<ClientDtoResponse> response = registerClient();

        ClientDtoResponse client = response.getBody();
        assertAll(
                () -> assertTrue(response.getHeaders().containsKey("Set-Cookie")),
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("Петр", client.getFirstName()),
                () -> assertEquals("Петров", client.getLastName()),
                () -> assertEquals("Петрович", client.getPatronymic()),
                () -> assertEquals("petr@mail.ru", client.getEmail()),
                () -> assertEquals("+77777777777", client.getPhone()),
                () -> assertEquals(UserType.CLIENT, client.getUserType()));
    }

    @Test
    public void testUpdateClient() {
        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        HttpEntity<UpdateClientDtoRequest> updateRequest = new HttpEntity<>
                (new UpdateClientDtoRequest(null, null, null, "password", "new_password", "updatedPetr@mail.ru", "+7-888-777-77-77"), headers);

        ResponseEntity<ClientDtoResponse> response = template.exchange("http://localhost:8080/api/clients", HttpMethod.PUT, updateRequest, ClientDtoResponse.class);
        ClientDtoResponse client = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("Петр", client.getFirstName()),
                () -> assertEquals("Петров", client.getLastName()),
                () -> assertEquals("Петрович", client.getPatronymic()),
                () -> assertEquals("updatedPetr@mail.ru", client.getEmail()),
                () -> assertEquals("+78887777777", client.getPhone()),
                () -> assertEquals(UserType.CLIENT, client.getUserType()));
    }

    @Test
    public void testGetAllClients(){
        String sessionID = registerAdmin().getHeaders().get("Set-Cookie").get(0);

        for(int i = 1; i < 4; i++){
            HttpEntity<RegisterClientDtoRequest> request = new HttpEntity<>
                    (new RegisterClientDtoRequest("Петр", "Петров", "Петрович", "login" + i, "password", "petr@mail.ru", "+7-777-777-77-77"));

            template.postForEntity("http://localhost:8080/api/clients", request, ClientDtoResponse.class);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        ResponseEntity<GetAllClientsDtoResponse> clientsResponseEntity = template.exchange("http://localhost:8080/api/clients", HttpMethod.GET, new HttpEntity<>(headers), GetAllClientsDtoResponse.class);

        GetAllClientsDtoResponse clientsResponse = clientsResponseEntity.getBody();

        assertAll(
                () -> assertEquals(200, clientsResponseEntity.getStatusCodeValue()),
                () -> assertEquals(3,clientsResponse.getClientList().size()),
                () -> assertTrue(clientsResponse.getClientList().stream().allMatch(item -> item.getEmail().equals("petr@mail.ru")
                        && item.getFirstName().equals("Петр")
                        && item.getLastName().equals("Петров")
                        && item.getPatronymic().equals("Петрович")
                        && item.getPhone().equals("+77777777777")
                        && item.getUserType().equals(UserType.CLIENT)))
        );
    }

    @Test
    public void testRegisterWrongClient(){
        HttpEntity<RegisterClientDtoRequest> registerRequest = new HttpEntity<>(new RegisterClientDtoRequest(null, "wrong last name", "ПетровичПетровичПетровичПетрович", "login6-", "", "petrmail.ru", "+7-777-777-707-77"));

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.postForEntity("http://localhost:8080/api/clients", registerRequest, ClientDtoResponse.class);
        });

        GlobalErrorHandler.ValidationExcResponse responses = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ValidationExcResponse.class);
        List<GlobalErrorHandler.ErrorResponse> errorResponses = responses.getErrorResponses();

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals(8, errorResponses.size()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("Can't be null.")).count()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("Only rus symbols, minus and space character.")).count()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("You entered wrong name")).count()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("Only rus/eng and number characters.")).count()),
                () -> assertEquals(2, errorResponses.stream().filter(item -> item.getMessage().equals("Can't be blank.")).count()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("Wrong email format")).count()),
                () -> assertEquals(1, errorResponses.stream().filter(item -> item.getMessage().equals("+7|8 xxx-xxx-xx-xx ( '-' character not required)")).count())
        );

    }

    @Test
    public void testGetAllClientsByClientSessionID(){

        String sessionID = registerClient().getHeaders().get("Set-Cookie").get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionID);

        HttpClientErrorException exc = assertThrows(HttpClientErrorException.class, () -> {
            template.exchange("http://localhost:8080/api/clients", HttpMethod.GET, new HttpEntity<>(headers), GetAllClientsDtoResponse.class);
        });

        GlobalErrorHandler.ErrorResponse errorResponse = gson.fromJson(exc.getResponseBodyAsString(), GlobalErrorHandler.ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, exc.getStatusCode().value()),
                () -> assertEquals("You have no rights to do this operation.", errorResponse.getMessage())
        );



    }



}