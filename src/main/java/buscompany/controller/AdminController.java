package buscompany.controller;

import buscompany.dto.request.RegisterAdminDtoRequest;
import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.dto.response.UpdateAdminDtoResponse;
import buscompany.exception.ServerException;
import buscompany.service.AdminService;
import buscompany.service.ClientService;
import buscompany.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/admins")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AdminDtoResponse registerAdmin(@RequestBody @Valid RegisterAdminDtoRequest admin, HttpServletResponse servletResponse) throws ServerException {
        return adminService.registerAdmin(admin, servletResponse);
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UpdateAdminDtoResponse updateAdmin(@RequestBody @Valid UpdateAdminDtoRequest updateAdminDtoRequest, @CookieValue("JAVASESSIONID") String sessionID){
        return adminService.updateAdmin(updateAdminDtoRequest, sessionID);
    }

}
