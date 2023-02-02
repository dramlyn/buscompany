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
public class AdminDtoResponse extends UserDtoResponse {

    private String position;

    public AdminDtoResponse(int id, String firstName, String lastName, String patronymic, String position){
        super(id, firstName, lastName, patronymic, UserType.ADMIN);
        this.position = position;
    }


}
