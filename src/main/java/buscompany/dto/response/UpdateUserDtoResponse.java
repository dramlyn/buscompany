package buscompany.dto.response;

import buscompany.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class UpdateUserDtoResponse {
    private String firstName;
    private String lastName;
    private String patronymic;
    private UserType userType;
}
