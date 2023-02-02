package buscompany.service;

import buscompany.daoimpl.SessionDaoImpl;
import buscompany.daoimpl.UserDaoImpl;
import buscompany.dto.request.LoginUserDtoRequest;
import buscompany.dto.response.UserDtoResponse;
import buscompany.mapstruct.AdminMapper;
import buscompany.mapstruct.ClientMapper;
import buscompany.model.Admin;
import buscompany.model.Client;
import buscompany.model.User;
import buscompany.utils.CookieUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class SessionService {

    private SessionDaoImpl sessionDao;

    private UserDaoImpl userDao;

    private CookieUtils cookieUtils;

    public UserDtoResponse login(LoginUserDtoRequest request, HttpServletResponse httpResponse) {
        User user = userDao.getUserByLoginAndPassword(request.getLogin(), request.getPassword());
        cookieUtils.setSession(httpResponse, user.getId());
        if (user.getClass().equals(Admin.class)) {
            return AdminMapper.INSTANCE.adminToAdminDtoResponse((Admin) user);
        }
        return ClientMapper.INSTANCE.clientToClientDtoResponse((Client) user);
    }

    public void logout(String sessionID) {
        cookieUtils.handleSessionID(sessionID);
        sessionDao.logout(sessionID);
    }

    public void leave(String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);
        sessionDao.leave(userID);
    }


}
