package buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserDtoRequest {
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "^[А-яёЁA-z\\d]+$")
    private String login;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String password;
}
