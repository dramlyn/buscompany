package buscompany.dto.request;

import buscompany.validator.CorrectName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class UpdateUserDtoRequest {
    @CorrectName
    @Pattern(regexp = "^[А-яёЁ\\s-]+$", message = "Only rus symbols, minus and space character.")
    private String firstName;
    @CorrectName
    @Pattern(regexp = "^[А-яёЁ\\s-]+$", message = "Only rus symbols, minus and space character.")
    private String lastName;
    @CorrectName
    @Pattern(regexp = "^[А-яёЁ\\s-]+$", message = "Only rus symbols, minus and space character.")
    private String patronymic;
    private String oldPassword;
    private String newPassword;
}
