package buscompany.dto.response;

import buscompany.model.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ClientDtoResponse extends UserDtoResponse {

    private String email;
    private String phone;

    public ClientDtoResponse(int id, String firstName, String lastName, String patronymic, String email, String phone){
        super(id, firstName, lastName, patronymic, UserType.CLIENT);
        this.email = email;
        this.phone = phone;
    }
}
