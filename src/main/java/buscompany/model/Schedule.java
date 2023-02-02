package buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Schedule {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String period;
}
