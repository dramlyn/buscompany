package buscompany.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class UpdateAdminDtoRequest extends UpdateUserDtoRequest {

    private String position;

    public UpdateAdminDtoRequest(String firstName, String lastName, String patronymic, String oldPassword, String newPassword, String position){
        super(firstName,lastName,patronymic,oldPassword,newPassword);
        this.position = position;
    }
}
