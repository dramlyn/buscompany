package buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PassengerDtoResponse {
    private String firstName;
    private String lastName;
    private String passport;
}
