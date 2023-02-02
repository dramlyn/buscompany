package buscompany.dto.response;

import buscompany.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.SortedSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripDtoResponse {
    private int id;
    private String fromStation;
    private String toStation;
    private LocalTime start;
    private int duration;
    private int price;
    private BusDtoResponse bus;
    private boolean approved;
    private LinkedList<LocalDate> dates;
    private ScheduleDtoResponse schedule;

}
