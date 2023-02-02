package buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDtoRequest {
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])", message = "YYYY-MM(1-12)-DD(1-31)")
    private String fromDate;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])", message = "YYYY-MM(1-12)-DD(1-31)")
    private String toDate;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "^(([dD][aA][iI][lL][yY])" +
            "|([eE][vV][Ee][nN])" +
            "|([oO][dD][dD])" +
            "|(^\\s*(((Sun|Mon|Tue|Wed|Thu|Fri|Sat)(\\s*,\\s*))+(Sun|Mon|Tue|Wed|Thu|Fri|Sat))|(Sun|Mon|Tue|Wed|Thu|Fri|Sat)\\s*$)" +
            "|^\\s*((([1-9]|0[1-9]|1[0-9]|2[0-9]|3[0-1])(\\s*,\\s*))+([1-9]|0[1-9]|1[0-9]|2[0-9]|3[0-1]))|([1-9]|0[1-9]|1[0-9]|2[0-9]|3[0-1])\\s*$)$"
    , message = "Wrong period format.")
    private String period;
}
