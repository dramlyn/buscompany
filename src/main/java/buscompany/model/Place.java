package buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Place {
    private int orderID;
    private TripDate tripDate;
    private String ticket;
    private String lastName;
    private String firstName;
    private String passport;
    private int place;
}
