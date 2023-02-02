package buscompany.mapstruct;

import buscompany.dto.request.RegisterAdminDtoRequest;
import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.dto.response.AdminDtoResponse;
import buscompany.dto.response.UpdateAdminDtoResponse;
import buscompany.model.Admin;
import buscompany.model.UserType;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-09T17:24:21+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class AdminMapperImpl implements AdminMapper {

    @Override
    public Admin adminDtoToAdmin(RegisterAdminDtoRequest adminDto) {
        if ( adminDto == null ) {
            return null;
        }

        Admin admin = new Admin();

        if ( adminDto.getPatronymic() != null ) {
            admin.setPatronymic( adminDto.getPatronymic() );
        }
        else {
            admin.setPatronymic( "" );
        }
        admin.setFirstName( adminDto.getFirstName() );
        admin.setLastName( adminDto.getLastName() );
        admin.setLogin( adminDto.getLogin() );
        admin.setPassword( adminDto.getPassword() );
        admin.setPosition( adminDto.getPosition() );

        return admin;
    }

    @Override
    public AdminDtoResponse adminToAdminDtoResponse(Admin admin) {
        if ( admin == null ) {
            return null;
        }

        AdminDtoResponse adminDtoResponse = new AdminDtoResponse();

        adminDtoResponse.setId( admin.getId() );
        adminDtoResponse.setFirstName( admin.getFirstName() );
        adminDtoResponse.setLastName( admin.getLastName() );
        adminDtoResponse.setPatronymic( admin.getPatronymic() );
        adminDtoResponse.setPosition( admin.getPosition() );

        adminDtoResponse.setUserType( UserType.ADMIN );

        return adminDtoResponse;
    }

    @Override
    public Admin updateAdminDtoToAdmin(UpdateAdminDtoRequest adminDto) {
        if ( adminDto == null ) {
            return null;
        }

        Admin admin = new Admin();

        admin.setPassword( adminDto.getNewPassword() );
        admin.setFirstName( adminDto.getFirstName() );
        admin.setLastName( adminDto.getLastName() );
        admin.setPatronymic( adminDto.getPatronymic() );
        admin.setPosition( adminDto.getPosition() );

        return admin;
    }

    @Override
    public UpdateAdminDtoResponse adminToUpdateAdminDtoResponse(Admin admin) {
        if ( admin == null ) {
            return null;
        }

        UpdateAdminDtoResponse updateAdminDtoResponse = new UpdateAdminDtoResponse();

        updateAdminDtoResponse.setFirstName( admin.getFirstName() );
        updateAdminDtoResponse.setLastName( admin.getLastName() );
        updateAdminDtoResponse.setPatronymic( admin.getPatronymic() );
        updateAdminDtoResponse.setPosition( admin.getPosition() );

        updateAdminDtoResponse.setUserType( UserType.ADMIN );

        return updateAdminDtoResponse;
    }
}
