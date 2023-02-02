package buscompany.service;

import buscompany.daoimpl.UserDaoImpl;
import buscompany.dto.response.UserDtoResponse;
import buscompany.mapstruct.AdminMapper;
import buscompany.mapstruct.ClientMapper;
import buscompany.model.Admin;
import buscompany.model.Client;
import buscompany.model.User;
import buscompany.utils.CookieUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserDaoImpl userDao;

    private CookieUtils cookieUtils;

    public UserDtoResponse getUser(String sessionID){
        cookieUtils.handleSessionID(sessionID);
        User user = userDao.getUserBySessionId(sessionID);
        if(user.getClass().equals(Admin.class)){
            return AdminMapper.INSTANCE.adminToAdminDtoResponse((Admin) user);
        }
        return ClientMapper.INSTANCE.clientToClientDtoResponse((Client) user);
    }



}
