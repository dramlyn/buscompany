package buscompany.dto.response;

import buscompany.model.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class GetAllClientsDtoResponse {
    private List<ClientDtoResponse> clientList = new ArrayList<>();

    public GetAllClientsDtoResponse(List<Client> clientList){
        for(Client client: clientList){
            this.clientList.add(new ClientDtoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getPhone()));
        }
    }
}
