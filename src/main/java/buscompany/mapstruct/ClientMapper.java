package buscompany.mapstruct;

import buscompany.dto.request.RegisterClientDtoRequest;
import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.dto.response.UpdateClientDtoResponse;
import buscompany.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client clientDtoToClient(RegisterClientDtoRequest dtoRequest);

    @Mapping(target = "userType", constant = "CLIENT")
    ClientDtoResponse clientToClientDtoResponse(Client client);

    @Mapping(source = "clientDto.newPassword", target = "password")
    Client updateClientDtoToClient(UpdateClientDtoRequest clientDto);

    @Mapping(target = "userType", constant = "CLIENT")
    UpdateClientDtoResponse clientToUpdateClientDtoResponse(Client client);
}
