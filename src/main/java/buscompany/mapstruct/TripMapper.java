package buscompany.mapstruct;


import buscompany.dto.request.ScheduleDtoRequest;
import buscompany.dto.request.TripDtoRequest;
import buscompany.dto.response.TripDtoResponse;
import buscompany.model.Bus;
import buscompany.model.Trip;
import buscompany.model.TripDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

@Mapper(uses = ScheduleMapper.class)
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    default Trip tripDtoRequestToTrip(TripDtoRequest tripDtoRequest, int freePlaces) {
        Trip trip = new Trip(
                0,
                tripDtoRequest.getFromStation(),
                tripDtoRequest.getToStation(),
                LocalTime.parse(tripDtoRequest.getStart()),
                tripDtoRequest.getDuration(),
                tripDtoRequest.getPrice(),
                new Bus(tripDtoRequest.getBusName(), freePlaces),
                false,
                null,
                null
        );
        ScheduleDtoRequest schedule = tripDtoRequest.getSchedule();
        SortedSet<String> dates = tripDtoRequest.getDates();

        if (schedule == null && dates != null) {
            LinkedList<TripDate> tripDate = new LinkedList<>();
            for (String date : dates) {
                tripDate.add(new TripDate(0, LocalDate.parse(date), freePlaces));
                trip.setDates(tripDate);
            }
        }
        if (dates == null && schedule != null) {
            trip.setSchedule(ScheduleMapper.INSTANCE.scheduleDtoRequestToSchedule(schedule));
        }
        return trip;
    }

    default TripDtoResponse tripToTripDtoResponse(Trip trip) {
        TripDtoResponse tripDtoResponse = new TripDtoResponse(
                trip.getId(),
                trip.getFromStation(),
                trip.getToStation(),
                trip.getStart(),
                trip.getDuration(),
                trip.getPrice(),
                BusMapper.INSTANCE.busToBusDtoResponse(trip.getBus()),
                trip.isApproved(),
                null,
                ScheduleMapper.INSTANCE.scheduleToScheduleDtoResponse(trip.getSchedule())
        );
        LinkedList<LocalDate> dates = new LinkedList<>();
        if(trip.getDates() != null) {
            for (TripDate tripDate : trip.getDates()) {
                dates.add(tripDate.getDate());
            }
        }
        tripDtoResponse.setDates(dates);
        return tripDtoResponse;
    }

    default List<TripDtoResponse> tripListToTripDtoResponseList(List<Trip> tripList) {
        List<TripDtoResponse> output = new ArrayList<>();
        for (Trip trip : tripList) {
            output.add(tripToTripDtoResponse(trip));
        }
        return output;
    }


}

