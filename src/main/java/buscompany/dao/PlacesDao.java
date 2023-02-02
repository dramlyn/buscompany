package buscompany.dao;

import buscompany.model.Place;

import java.time.LocalDate;
import java.util.List;

public interface PlacesDao {


    List<Integer> getFreePlacesList(int tripID, LocalDate date);

    int insertPlace(Place place, int passengerID);


    int getFreePlaces(int tripID, LocalDate date);

    int isPlacedByThisPassenger(int orderID, int passengerID);

    void deletePassengerPlace(int orderID, int passengerID);

    int checkPassenger(int orderID, String passport);

    int checkPlacedByAnotherPassenger(int place, int tripID, LocalDate date);

    int getOccupiedPlacesByOrder(int orderID);

    void updateFreePlaces(int newOccupiedPlaces, int tripID, LocalDate date);

    void deleteOrderPassengersPlaces(int orderID);

    int checkPassportFirstAndLastName(int orderID, String passport, String firstName, String lastName);
}
