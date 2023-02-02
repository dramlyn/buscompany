package buscompany.dao;

import buscompany.model.Trip;

import java.util.List;

public interface TripDao {
    Trip addTrip(Trip trip);

    Trip selectTripById(int tripId);

    void updateTrip(Trip trip);

    void deleteTrip(int tripID);

    void approveTrip(int tripID);

    List<Trip> getTripList(String fromStation, String toStation,
                           String busName, boolean approved);

}
