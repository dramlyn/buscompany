package buscompany.mapstruct;

import buscompany.dto.request.RegisterAdminDtoRequest;
import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.dto.response.UpdateAdminDtoResponse;
import buscompany.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "patronymic", defaultValue = "")
    Admin adminDtoToAdmin(RegisterAdminDtoRequest adminDto);

    @Mapping(target = "userType", constant = "ADMIN")
    AdminDtoResponse adminToAdminDtoResponse(Admin admin);

    @Mapping(source = "adminDto.newPassword", target = "password")
    Admin updateAdminDtoToAdmin(UpdateAdminDtoRequest adminDto);

    @Mapping(target = "userType", constant = "ADMIN")
    UpdateAdminDtoResponse adminToUpdateAdminDtoResponse(Admin admin);
}
