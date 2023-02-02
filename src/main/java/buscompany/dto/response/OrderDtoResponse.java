package buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDtoResponse {
    private int id;
    private int tripID;
    private String fromStation;
    private String toStation;
    private String busName;
    private LocalDate date;
    private LocalTime start;
    private int duration;
    private int price;
    private int totalPrice;
    private List<PassengerDtoResponse> passengers;
}
