package buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.SortedSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateTripDtoRequest {
    private String busName;
    private String fromStation;
    private String toStation;
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message = "HH:MM")
    private String start;
    private int duration;
    private int price;
    @Valid
    private ScheduleDtoRequest schedule;
    private SortedSet<@Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])", message = "YYYY-MM-DD") String> dates;

}
