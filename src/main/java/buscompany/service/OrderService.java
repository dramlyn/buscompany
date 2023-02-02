package buscompany.service;

import buscompany.daoimpl.OrderDaoImpl;
import buscompany.daoimpl.PlacesDaoImpl;
import buscompany.daoimpl.TripDaoImpl;
import buscompany.dto.request.OrderDtoRequest;
import buscompany.dto.request.PassengerDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mapstruct.OrderMapper;
import buscompany.model.Order;
import buscompany.model.Trip;
import buscompany.model.TripDate;
import buscompany.utils.CookieUtils;
import buscompany.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private ServiceUtils serviceUtils;

    private OrderDaoImpl orderDao;

    private TripDaoImpl tripDao;

    private CookieUtils cookieUtils;

    private PlacesDaoImpl placesDao;

    public OrderDtoResponse insertOrder(OrderDtoRequest orderDtoRequest, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (!serviceUtils.isClient(userID)) {
            throw new ServerException(ServerErrorCode.NO_RIGHTS, "sessionID", "You have no right for this operation.");
        }
        List<PassengerDtoRequest> passengers = orderDtoRequest.getPassengers();

        Set<String> passportSet = passengers.stream().map(PassengerDtoRequest::getPassport).collect(Collectors.toSet());

        if(passportSet.size() != passengers.size()){
            throw new ServerException(ServerErrorCode.WRONG_PASSENGERS_PASSPORT, "passport", "Some passengers in this order" +
                    " have the same passports");
        }

        Trip trip = tripDao.selectTripById(orderDtoRequest.getTripID());
        serviceUtils.validateTrip(trip);
        if (!trip.isApproved()) {
            throw new ServerException(ServerErrorCode.APPROVED_TRIP, "approved", "You can't use this trip. This trip is not approved.");
        }

        boolean rightDate = trip.getDates().stream().map(TripDate::getDate).anyMatch(date -> date.equals(LocalDate.parse(orderDtoRequest.getDate())));
        if (!rightDate) {
            throw new ServerException(ServerErrorCode.WRONG_DATE, "date", "In your order you entered wrong date.");
        }

        Order order = OrderMapper.INSTANCE.orderDtoRequestToOrder(orderDtoRequest, trip);
        int tripID = trip.getId();
        TripDate tripDate = order.getTripDate();
        LocalDate date = tripDate.getDate();
        int freePlaces = placesDao.getFreePlaces(tripID, date);

        tripDate.setFreePlaces(freePlaces);

        orderDao.insertOrder(order, userID);
        return OrderMapper.INSTANCE.orderToOrderDtoResponse(order);
    }

    public List<OrderDtoResponse> getOrderList(String fromStation, String toStation, String busName, String fromDate, String toDate, int clientID, String sessionID) {
        int userID = cookieUtils.handleSessionID(sessionID);

        if (serviceUtils.isAdmin(userID)) {
            List<Order> orderList = serviceUtils.makeOrderList(
                    orderDao.getOrderList(fromStation, toStation, busName, clientID), fromDate, toDate);
            return OrderMapper.INSTANCE.orderListToOrderDtoResponseList(orderList);
        }

        List<Order> orderList = serviceUtils.makeOrderList(
                orderDao.getOrderList(fromStation, toStation, busName, userID), fromDate, toDate);
        return OrderMapper.INSTANCE.orderListToOrderDtoResponseList(orderList);
    }

    public void cancelOrder(String sessionID, int orderID) {
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

        int tripID = orderDao.getTripIDByOrderID(orderID);
        LocalDate date = orderDao.getOrderDateByOrderID(orderID);

        int occupiedPlaces = placesDao.getOccupiedPlacesByOrder(orderID);
        if (occupiedPlaces != 0) {
            placesDao.updateFreePlaces(-occupiedPlaces, tripID, date);
        }
        placesDao.deleteOrderPassengersPlaces(orderID);
        orderDao.deleteOrder(orderID);
    }
}
