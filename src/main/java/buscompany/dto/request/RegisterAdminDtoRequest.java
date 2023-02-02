package buscompany.dto.request;

import buscompany.validator.CorrectName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/*@AllArgsConstructor
@NoArgsConstructor
@Data*/
public class RegisterAdminDtoRequest {
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
    private String position;

    public RegisterAdminDtoRequest(String firstName, String lastName, String patronymic, String login, String password, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.position = position;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterAdminDtoRequest that = (RegisterAdminDtoRequest) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(patronymic, that.patronymic) && Objects.equals(login, that.login) && Objects.equals(password, that.password) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, login, password, position);
    }

    @Override
    public String toString() {
        return "RegisterAdminDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
