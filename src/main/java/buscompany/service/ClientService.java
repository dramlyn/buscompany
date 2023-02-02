package buscompany.service;

import buscompany.daoimpl.ClientDaoImpl;
import buscompany.daoimpl.SessionDaoImpl;
import buscompany.daoimpl.UserDaoImpl;
import buscompany.dto.request.RegisterClientDtoRequest;
import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.dto.response.ClientDtoResponse;
import buscompany.dto.response.GetAllClientsDtoResponse;
import buscompany.dto.response.UpdateClientDtoResponse;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.ClientMapper;
import buscompany.model.Client;
import buscompany.model.UserType;
import buscompany.utils.CookieUtils;
import buscompany.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    private ClientDaoImpl clientDao;

    private UserDaoImpl userDao;

    private CookieUtils cookieUtils;

    private ServiceUtils serviceUtils;


    public ClientDtoResponse registerClient(RegisterClientDtoRequest request, HttpServletResponse servletResponse) {
        Client client = ClientMapper.INSTANCE.clientDtoToClient(request);
        client.setPhone(client.getPhone().replaceAll("-", ""));
        clientDao.insert(client);
        cookieUtils.setSession(servletResponse, client.getId());
        return ClientMapper.INSTANCE.clientToClientDtoResponse(client);
    }

    public GetAllClientsDtoResponse getAllClients(String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no rights to do this operation.");
        }

        return new GetAllClientsDtoResponse(clientDao.getAllClients());
    }

    public UpdateClientDtoResponse updateClient(UpdateClientDtoRequest updateClientDtoRequest, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isClient(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }

        if (!updateClientDtoRequest.getOldPassword().equals(userDao.selectPasswordByUserID(userID))) {
            throw new ServerException(ServerErrorCode.WRONG_OLD_PASSWORD, "oldPassword", "You entered wrong old password.");
        }

        Client client = clientDao.getClientById(userID);
        serviceUtils.updateClient(client, updateClientDtoRequest);

        clientDao.update(client, userID);

        return ClientMapper.INSTANCE.clientToUpdateClientDtoResponse(clientDao.getClientById(userID));

    }


}
