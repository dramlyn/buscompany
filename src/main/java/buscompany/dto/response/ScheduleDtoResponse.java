package buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDtoResponse {
    private String fromDate;
    private String toDate;
    private String period;
}
