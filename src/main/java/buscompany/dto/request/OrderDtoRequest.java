package buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDtoRequest {
    @NotNull(message = "Can't be null.")
    private int tripID;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])", message = "YYYY-MM(1-12)-DD(1-31)")
    private String date;
    @Valid
    private List<PassengerDtoRequest> passengers;
}
