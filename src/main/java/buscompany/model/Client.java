package buscompany.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Getter
public class Client extends User {
    private String email;
    private String phone;

    public Client(int id, String firstName, String lastName, String patronymic, String login, String password, String email, String phone){
        super(id, firstName, lastName, patronymic, login, password);
        this.email = email;
        this.phone = phone;
    }

}
