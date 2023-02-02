package buscompany.mapstruct;

import buscompany.dto.request.RegisterClientDtoRequest;
import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.dto.response.UpdateClientDtoResponse;
import buscompany.model.Client;
import buscompany.model.UserType;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-09T17:24:21+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client clientDtoToClient(RegisterClientDtoRequest dtoRequest) {
        if ( dtoRequest == null ) {
            return null;
        }

        Client client = new Client();

        client.setFirstName( dtoRequest.getFirstName() );
        client.setLastName( dtoRequest.getLastName() );
        client.setPatronymic( dtoRequest.getPatronymic() );
        client.setLogin( dtoRequest.getLogin() );
        client.setPassword( dtoRequest.getPassword() );
        client.setEmail( dtoRequest.getEmail() );
        client.setPhone( dtoRequest.getPhone() );

        return client;
    }

    @Override
    public ClientDtoResponse clientToClientDtoResponse(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientDtoResponse clientDtoResponse = new ClientDtoResponse();

        clientDtoResponse.setId( client.getId() );
        clientDtoResponse.setFirstName( client.getFirstName() );
        clientDtoResponse.setLastName( client.getLastName() );
        clientDtoResponse.setPatronymic( client.getPatronymic() );
        clientDtoResponse.setEmail( client.getEmail() );
        clientDtoResponse.setPhone( client.getPhone() );

        clientDtoResponse.setUserType( UserType.CLIENT );

        return clientDtoResponse;
    }

    @Override
    public Client updateClientDtoToClient(UpdateClientDtoRequest clientDto) {
        if ( clientDto == null ) {
            return null;
        }

        Client client = new Client();

        client.setPassword( clientDto.getNewPassword() );
        client.setFirstName( clientDto.getFirstName() );
        client.setLastName( clientDto.getLastName() );
        client.setPatronymic( clientDto.getPatronymic() );
        client.setEmail( clientDto.getEmail() );
        client.setPhone( clientDto.getPhone() );

        return client;
    }

    @Override
    public UpdateClientDtoResponse clientToUpdateClientDtoResponse(Client client) {
        if ( client == null ) {
            return null;
        }

        UpdateClientDtoResponse updateClientDtoResponse = new UpdateClientDtoResponse();

        updateClientDtoResponse.setFirstName( client.getFirstName() );
        updateClientDtoResponse.setLastName( client.getLastName() );
        updateClientDtoResponse.setPatronymic( client.getPatronymic() );
        updateClientDtoResponse.setPhone( client.getPhone() );
        updateClientDtoResponse.setEmail( client.getEmail() );

        updateClientDtoResponse.setUserType( UserType.CLIENT );

        return updateClientDtoResponse;
    }
}
