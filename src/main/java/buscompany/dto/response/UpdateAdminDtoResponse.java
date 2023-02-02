package buscompany.dto.response;

import buscompany.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAdminDtoResponse extends UpdateUserDtoResponse {

    private String position;

    public UpdateAdminDtoResponse(String firstName, String lastName, String patronymic, String position){
        super(firstName, lastName, patronymic, UserType.ADMIN);
        this.position = position;
    }
}
