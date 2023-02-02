package buscompany.dao;

import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.model.Client;

import java.util.List;

public interface ClientDao {

    Client insert(Client client);

    List<Client> getAllClients();

    Client getClientById(int clientId);

    void update(Client updateClient, int clientId);
}
