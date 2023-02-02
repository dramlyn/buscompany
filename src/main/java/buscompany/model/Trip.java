package buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.SortedSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Trip {
    private int id;
    private String fromStation;
    private String toStation;
    private LocalTime start;
    private int duration;
    private int price;
    private Bus bus;
    private boolean approved;
    private Schedule schedule;
    private LinkedList<TripDate> dates;
}
