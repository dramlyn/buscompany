package buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.SortedSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientTripDtoResponse {
    private int tripId;
    private String fromStation;
    private String toStation;
    private String start;
    private String duration;
    private float price;
    private BusDtoResponse bus;
    private SortedSet<String> dates;

    private ScheduleDtoResponse schedule;
}
