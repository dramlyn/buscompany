package buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceDtoResponse {
    private int orderID;
    private String ticket;
    private String lastName;
    private String firstName;
    private String passport;
    private int place;
}
