package buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.SortedSet;

/*@AllArgsConstructor
@NoArgsConstructor
@Data*/
public class TripDtoRequest {
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String busName;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String fromStation;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    private String toStation;
    @NotNull(message = "Can't be null.")
    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message = "HH(00-23):MM(00-59)")
    private String start;
    @NotNull(message = "Can't be null.")
    private int duration;
    @NotNull(message = "Can't be null.")
    private int price;
    @Valid
    private ScheduleDtoRequest schedule;
    private SortedSet<@Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])", message = "YYYY-MM(1-12)-DD(1-31)") String> dates;

    public TripDtoRequest(String busName, String fromStation, String toStation, String start, int duration, int price, ScheduleDtoRequest schedule, SortedSet<@Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])", message = "YYYY-MM(1-12)-DD(1-31)") String> dates) {
        this.busName = busName;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.start = start;
        this.duration = duration;
        this.price = price;
        this.schedule = schedule;
        this.dates = dates;
    }

    public TripDtoRequest() {
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ScheduleDtoRequest getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDtoRequest schedule) {
        this.schedule = schedule;
    }

    public SortedSet<String> getDates() {
        return dates;
    }

    public void setDates(SortedSet<String> dates) {
        this.dates = dates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripDtoRequest that = (TripDtoRequest) o;
        return duration == that.duration && price == that.price && Objects.equals(busName, that.busName) && Objects.equals(fromStation, that.fromStation) && Objects.equals(toStation, that.toStation) && Objects.equals(start, that.start) && Objects.equals(schedule, that.schedule) && Objects.equals(dates, that.dates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(busName, fromStation, toStation, start, duration, price, schedule, dates);
    }

    @Override
    public String toString() {
        return "TripDtoRequest{" +
                "busName='" + busName + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", start='" + start + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                ", schedule=" + schedule +
                ", dates=" + dates +
                '}';
    }
}
