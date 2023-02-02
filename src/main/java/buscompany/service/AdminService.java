package buscompany.service;

import buscompany.daoimpl.AdminDaoImpl;
import buscompany.daoimpl.UserDaoImpl;
import buscompany.dto.request.RegisterAdminDtoRequest;
import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.dto.response.UpdateAdminDtoResponse;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.AdminMapper;
import buscompany.model.Admin;
import buscompany.utils.CookieUtils;
import buscompany.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class AdminService {


    private AdminDaoImpl adminDao;

    private ServiceUtils serviceUtils;

    private UserDaoImpl userDao;

    private CookieUtils cookieUtils;

    public AdminDtoResponse registerAdmin(RegisterAdminDtoRequest request, HttpServletResponse servletResponse) throws ServerException {

        Admin admin = AdminMapper.INSTANCE.adminDtoToAdmin(request);
        adminDao.insert(admin);
        cookieUtils.setSession(servletResponse, admin.getId());
        return AdminMapper.INSTANCE.adminToAdminDtoResponse(admin);
    }

    public UpdateAdminDtoResponse updateAdmin(UpdateAdminDtoRequest updateRequest, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isAdmin(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }
        if (!updateRequest.getOldPassword().equals(userDao.selectPasswordByUserID(userID))) {
            throw new ServerException(ServerErrorCode.WRONG_OLD_PASSWORD, "oldPassword", "You entered wrong old password.");
        }

        Admin admin = adminDao.getAdminById(userID);
        serviceUtils.updateAdmin(admin, updateRequest);
        adminDao.update(admin, userID);
        return AdminMapper.INSTANCE.adminToUpdateAdminDtoResponse(adminDao.getAdminById(userID));
    }
}
