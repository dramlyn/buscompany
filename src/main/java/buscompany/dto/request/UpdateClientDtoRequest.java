package buscompany.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class UpdateClientDtoRequest extends UpdateUserDtoRequest {


    @Email
    private String email;

    @Pattern(regexp = "((\\+7|8)[\\-]?)(\\d{3})([\\-]?)(\\d{3})([\\-]?)(\\d{2})([\\-]?)(\\d{2})([\\-]?)", message = "+7|8 xxx-xxx-xx-xx ( '-' character not required)")
    private String phone;

    public UpdateClientDtoRequest(String firstName, String lastName, String patronymic, String oldPassword, String newPassword, String email, String phone){
        super(firstName, lastName, patronymic, oldPassword, newPassword);
        this.email = email;
        this.phone = phone;
    }
}
