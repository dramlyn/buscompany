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
public class UpdateClientDtoResponse extends UpdateUserDtoResponse{

    private String phone;
    private String email;

    public UpdateClientDtoResponse(String firstName, String lastName, String patronymic, String phone, String email){
        super(firstName, lastName, patronymic, UserType.CLIENT);
        this.phone = phone;
        this.email = email;
    }
}
