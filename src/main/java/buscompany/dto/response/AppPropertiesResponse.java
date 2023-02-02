package buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppPropertiesResponse {
    private int maxNameLength;
    private int minPasswordLength;
}
