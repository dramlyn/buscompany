package buscompany.service;

import buscompany.daoimpl.BusDaoImpl;
import buscompany.daoimpl.OrderDaoImpl;
import buscompany.daoimpl.PlacesDaoImpl;
import buscompany.dto.request.PlaceDtoRequest;
import buscompany.dto.response.PlaceDtoResponse;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.PlacesMapper;
import buscompany.model.Place;
import buscompany.model.TripDate;
import buscompany.utils.CookieUtils;
import buscompany.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlacesService {

    private PlacesDaoImpl placesDao;
    private BusDaoImpl busDao;
    private OrderDaoImpl orderDao;

    private ServiceUtils serviceUtils;

    private CookieUtils cookieUtils;

    public List<Integer> getFreePlaces(String sessionID, int orderID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isClient(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }
        if (orderDao.checkOrderByOrderID(orderID) == 0) {
            throw new ServerException(ServerErrorCode.WRONG_ORDER_ID, "orderID", "You entered wrong orderID.");
        }

        if (orderDao.checkClientIDInOrder(orderID, userID) == 0) {
            throw new ServerException(ServerErrorCode.WRONG_ORDER_ID, "orderID", "You entered not yours orderID.");
        }

        /*List<Integer> occupiedPlaces = placesDao.getFreePlacesList(orderDao.getTripIDByOrderID(orderID),
                orderDao.getOrderDateByOrderID(orderID));
        int placeCount = busDao.getPlaceCountByBusName(orderDao.getBusNameByOrderID(orderID));

        List<Integer> freePlaces = new ArrayList<>();

        for (int i = 1; i <= placeCount; i++) {
            if (!occupiedPlaces.contains(i)) {
                freePlaces.add(i);
            }
        }*/

        return placesDao.getFreePlacesList(orderDao.getTripIDByOrderID(orderID), orderDao.getOrderDateByOrderID(orderID));
    }

    public PlaceDtoResponse choosePlace(PlaceDtoRequest placeDtoRequest, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isClient(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }
        int orderID = placeDtoRequest.getOrderID();

        if (orderDao.checkOrderByOrderID(orderID) == 0) {
            throw new ServerException(ServerErrorCode.WRONG_ORDER_ID, "orderID", "You entered wrong orderID.");
        }

        if (orderDao.checkClientIDInOrder(placeDtoRequest.getOrderID(), userID) == 0) {
            throw new ServerException(ServerErrorCode.WRONG_ORDER_ID, "orderID", "You entered not yours orderID.");
        }

        String passport = placeDtoRequest.getPassport();

        if (placesDao.checkPassenger(orderID, passport) == 0) {
            throw new ServerException(ServerErrorCode.WRONG_PASSPORT, "passport", "Passenger with this passport not exist.");
        }
        
        if(placesDao.checkPassportFirstAndLastName(orderID, passport, placeDtoRequest.getFirstName(), placeDtoRequest.getLastName()) == 0){
            throw new ServerException(ServerErrorCode.WRONG_NAME, "name", "This passport doesn't include this lastName and firstName");
        }


        String busName = orderDao.getBusNameByOrderID(orderID);
        int placeCount = busDao.getPlaceCountByBusName(busName);

        LocalDate date = orderDao.getOrderDateByOrderID(orderID);
        int tripID = orderDao.getTripIDByOrderID(orderID);
        int freePlaces = placesDao.getFreePlaces(tripID, date);
        int passengerID = orderDao.getPassengerIDByOrderIDAndPassport(orderID, passport);

        if (placeDtoRequest.getPlace() > placeCount || placeDtoRequest.getPlace() < 0) {
            throw new ServerException(ServerErrorCode.WRONG_PLACE, "place", "You entered wrong place. 0 < place <= " + placeCount);
        }

        int isPlaced = placesDao.isPlacedByThisPassenger(orderID, passengerID);
        if (isPlaced == 1) {
            placesDao.deletePassengerPlace(orderID, passengerID);
        }

        Place place = PlacesMapper.INSTANCE.placeDtoRequestToPlace(placeDtoRequest);
        place.setTripDate(new TripDate(tripID, date, freePlaces));

        if(placesDao.insertPlace(place, passengerID) == 0){
            throw new ServerException(ServerErrorCode.OCCUPIED_PLACE, "place", "This place is occupied.");
        }
        place.setTicket("Билет " + tripID + "_" + place.getPlace());
        return PlacesMapper.INSTANCE.placeToPlaceDtoResponse(place);
    }

}
