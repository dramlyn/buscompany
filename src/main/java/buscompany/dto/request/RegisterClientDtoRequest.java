package buscompany.dto.request;

import buscompany.validator.CorrectName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterClientDtoRequest {
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @CorrectName
    @Pattern(regexp = "^[А-яёЁ\\s-]+$", message = "Only rus symbols, minus and space character.")
    private String firstName;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @CorrectName
    @Pattern(regexp = "^[А-яёЁ\\s-]+$", message = "Only rus symbols, minus and space character.")
    private String lastName;
    @CorrectName
    @Pattern(regexp = "^[А-яёЁ\\s-]+$", message = "Only rus symbols, minus and space character.")
    private String patronymic;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "^[А-яёЁA-z\\d]+$", message = "Only rus/eng and number characters.")
    private String login;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String password;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Email(message = "Wrong email format")
    private String email;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "((\\+7|8)[\\-]?)(\\d{3})([\\-]?)(\\d{3})([\\-]?)(\\d{2})([\\-]?)(\\d{2})([\\-]?)", message = "+7|8 xxx-xxx-xx-xx ( '-' character not required)")
    private String phone;
}
