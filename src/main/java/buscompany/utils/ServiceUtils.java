package buscompany.utils;

import buscompany.dao.SessionDao;
import buscompany.daoimpl.BusDaoImpl;
import buscompany.daoimpl.TripDaoImpl;
import buscompany.daoimpl.UserDaoImpl;
import buscompany.dto.request.*;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.BusMapper;
import buscompany.mapstruct.ScheduleMapper;
import buscompany.mapstruct.TripMapper;
import buscompany.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServiceUtils {

    private UserDaoImpl userDao;

    private SessionDao sessionDao;

    private BusDaoImpl busDao;

    private TripDaoImpl tripDao;

    public void updateAdmin(Admin admin, UpdateAdminDtoRequest updateAdminDtoRequest){
        admin.setFirstName(updateAdminDtoRequest.getFirstName());
        admin.setLastName(updateAdminDtoRequest.getLastName());
        admin.setPatronymic(updateAdminDtoRequest.getPatronymic());
        admin.setPassword(updateAdminDtoRequest.getNewPassword());
        admin.setPosition(updateAdminDtoRequest.getPosition());
    }

    public void updateClient(Client client, UpdateClientDtoRequest updateClientDtoRequest){
        client.setFirstName(updateClientDtoRequest.getFirstName());
        client.setLastName(updateClientDtoRequest.getLastName());
        client.setPatronymic(updateClientDtoRequest.getPatronymic());
        client.setPassword(updateClientDtoRequest.getNewPassword());
        client.setPhone(updateClientDtoRequest.getPhone().replaceAll("-", ""));
        client.setEmail(updateClientDtoRequest.getEmail());
    }

    public void updateTrip(Trip trip, UpdateTripDtoRequest tripDtoRequest){
        int placeCount = trip.getBus().getPlaceCount();
        int tripID = trip.getId();
        ScheduleDtoRequest schedule = tripDtoRequest.getSchedule();
        trip.setSchedule(null);
        if(schedule != null){
            trip.setSchedule(ScheduleMapper.INSTANCE.scheduleDtoRequestToSchedule(tripDtoRequest.getSchedule()));
        }
        SortedSet<String> dates = tripDtoRequest.getDates();
        trip.setDates(null);
        if(dates != null && !dates.isEmpty()){
            trip.setDates(tripDtoRequest.getDates().stream().map(date -> new TripDate(tripID, LocalDate.parse(date), placeCount)).collect(Collectors.toCollection(LinkedList::new)));
        }
        trip.setDuration(tripDtoRequest.getDuration());
        trip.setFromStation(tripDtoRequest.getFromStation());
        trip.setToStation(tripDtoRequest.getToStation());
        trip.setPrice(tripDtoRequest.getPrice());
        String start = tripDtoRequest.getStart();
        trip.setStart(null);
        if(start != null) {
            trip.setStart(LocalTime.parse(start));
        }
    }

    public void makeDatesBySchedule(Trip trip) {
        Schedule schedule = trip.getSchedule();
        int freePlaces = trip.getBus().getPlaceCount();
        String period = trip.getSchedule().getPeriod().toLowerCase();

        LinkedList<TripDate> dates = new LinkedList<>();

        LocalDate lastDate = schedule.getToDate();
        LocalDate startDate = schedule.getFromDate();

        if (lastDate.compareTo(startDate) < 0) {
            throw new ServerException(ServerErrorCode.WRONG_SCHEDULE, "date", "fromDate > toDate");
        }

        if (period.equals("daily")) {
            for(LocalDate curDate = startDate; !curDate.isAfter(lastDate); curDate = curDate.plusDays(1)){
                dates.add(new TripDate(0, curDate, freePlaces));
            }
            trip.setDates(dates);
            return;
        }

        if (period.equals("odd")) {
            for(LocalDate curDate = startDate; !curDate.isAfter(lastDate); curDate = curDate.plusDays(1)){
                if(curDate.getDayOfMonth() % 2 != 0) {
                    dates.add(new TripDate(0, curDate, freePlaces));
                }
            }
            trip.setDates(dates);
            return;
        }

        if (period.equals("even")) {
            for(LocalDate curDate = startDate; !curDate.isAfter(lastDate); curDate = curDate.plusDays(1)){
                if(curDate.getDayOfMonth() % 2 == 0) {
                    dates.add(new TripDate(0, curDate, freePlaces));
                }
            }
            trip.setDates(dates);
            return;
        }

        List<String> list = Arrays.stream(period.split(",")).map(String::trim).collect(Collectors.toList());

        boolean containsDates = Arrays.stream(new DateFormatSymbols().getShortWeekdays()).anyMatch(list::contains);

        if (containsDates) {
            for(LocalDate curDate = startDate; !curDate.isAfter(lastDate); curDate = curDate.plusDays(1)){
                String curDateDayOfWeek = startDate.getDayOfWeek().toString().toLowerCase().substring(0, 3);
                if(list.contains(curDateDayOfWeek)) {
                    dates.add(new TripDate(0, curDate, freePlaces));
                }
            }
            trip.setDates(dates);
            return;
        } else {

            for(LocalDate curDate = startDate; !curDate.isAfter(lastDate); curDate = curDate.plusDays(1)){
                String curDateDayOfWeek = "" + startDate.getDayOfMonth();
                if(list.contains(curDateDayOfWeek)) {
                    dates.add(new TripDate(0, curDate, freePlaces));
                }
            }
        }
        trip.setDates(dates);
    }

    public List<Trip> makeTripList(List<Trip> tripList, String fromDate, String toDate) {
        if (toDate == null && fromDate == null) {
            return tripList;
        }

        List<Trip> output = new ArrayList<>();

        if (fromDate != null && toDate == null) {
            for (Trip trip : tripList) {
                TripDate firstDate = trip.getDates().getFirst();
                if (firstDate.getDate().compareTo(LocalDate.parse(fromDate)) >= 0) {
                    output.add(trip);
                }
            }
            return output;
        }

        if (fromDate == null) {
            for (Trip trip : tripList) {
                TripDate lastDate = trip.getDates().getLast();
                if (lastDate.getDate().compareTo(LocalDate.parse(toDate)) <= 0) {
                    output.add(trip);
                }
            }
            return output;
        }

        for (Trip trip : tripList) {
            TripDate firstDate = trip.getDates().getFirst();
            TripDate lastDate = trip.getDates().getLast();
            if (firstDate.getDate().compareTo(LocalDate.parse(fromDate)) >= 0 && lastDate.getDate().compareTo(LocalDate.parse(toDate)) <= 0) {
                output.add(trip);
            }
        }
        return output;
    }

    public List<Order> makeOrderList(List<Order> orderList, String fromDate, String toDate) {
        if (fromDate == null && toDate == null) {
            return orderList;
        }

        List<Order> output = new ArrayList<>();

        if (fromDate != null && toDate == null) {
            for (Order order : orderList) {
                LocalDate date = order.getTripDate().getDate();
                if (date.compareTo(LocalDate.parse(fromDate)) >= 0) {
                    output.add(order);
                }
            }
            return output;
        }

        if (fromDate == null) {
            for (Order order : orderList) {
                LocalDate date = order.getTripDate().getDate();
                if (date.compareTo(LocalDate.parse(toDate)) <= 0) {
                    output.add(order);
                    if(orderList.isEmpty()){
                        break;
                    }
                }
            }
            return output;
        }

        for (Order order : orderList) {
            LocalDate date = order.getTripDate().getDate();
            if (date.compareTo(LocalDate.parse(fromDate)) >= 0 && date.compareTo(LocalDate.parse(toDate)) <= 0) {
                output.add(order);
            }
        }
        return output;
    }

    public boolean isAdmin(int userID) {
        return userDao.selectUserTypeById(userID).equals(UserType.ADMIN);
    }

    public boolean isClient(int userID) {
        return userDao.selectUserTypeById(userID).equals(UserType.CLIENT);
    }

    public void validateBus(Bus bus) {
        if (bus == null) {
            throw new ServerException(ServerErrorCode.WRONG_BUSNAME, "busName", "Bus with this bus name doesn't exist.");
        }
    }

    public void validateTrip(TripDtoRequest tripDtoRequest) {

        if ((tripDtoRequest.getSchedule() != null && tripDtoRequest.getDates() != null)
                || (tripDtoRequest.getSchedule() == null && tripDtoRequest.getDates() == null)) {
            throw new ServerException(ServerErrorCode.WRONG_SCHEDULE, "schedule", "Schedule or dates must be null.");
        }
    }

    public void validateTrip(TripDtoRequest tripDtoRequest, Trip checkTrip) {
        if ((tripDtoRequest.getSchedule() != null && tripDtoRequest.getDates() != null)
                || (tripDtoRequest.getSchedule() == null && tripDtoRequest.getDates() == null)) {
            throw new ServerException(ServerErrorCode.WRONG_SCHEDULE, "schedule", "Schedule or dates must be null.");
        }
        validateTrip(checkTrip);
        if (checkTrip.isApproved()) {
            throw new ServerException(ServerErrorCode.APPROVED_TRIP, "approved", "You can't update trip. This trip is approved.");
        }
    }

    public void validateTrip(UpdateTripDtoRequest tripDtoRequest, Trip checkTrip) {
        if (tripDtoRequest.getSchedule() != null && tripDtoRequest.getDates() != null) {
            throw new ServerException(ServerErrorCode.WRONG_SCHEDULE, "schedule", "Schedule or dates must be null.");
        }
        validateTrip(checkTrip);
        if (checkTrip.isApproved()) {
            throw new ServerException(ServerErrorCode.APPROVED_TRIP, "approved", "You can't update trip. This trip is approved.");
        }
    }

    public void validateTrip(Trip checkTrip) {
        if (checkTrip == null) {
            throw new ServerException(ServerErrorCode.WRONG_TRIPID, "tripID", "You entered wrong tripID.");
        }
    }


}
