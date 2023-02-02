package buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private int id;
    private TripDate tripDate;
    private String fromStation;
    private String toStation;
    private String busName;
    private LocalTime start;
    private int duration;
    private int price;
    private int totalPrice;
    private List<Passenger> passengers;
}
