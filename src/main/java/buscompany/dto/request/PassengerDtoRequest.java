package buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PassengerDtoRequest {
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String firstName;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String lastName;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String passport;
}
