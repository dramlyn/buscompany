package buscompany.model;

import lombok.*;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Admin extends User {
    private String position;

    public Admin(int id, String firstName, String lastName, String patronymic, String login, String password, String position){
        super(id, firstName, lastName, patronymic, login, password);
        this.position = position;
    }


}
