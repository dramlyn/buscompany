package buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class User implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
}
