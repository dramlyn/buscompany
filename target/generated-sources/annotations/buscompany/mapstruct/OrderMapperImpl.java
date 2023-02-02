package buscompany.mapstruct;

import buscompany.dto.request.OrderDtoRequest;
import buscompany.dto.request.PassengerDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.dto.response.PassengerDtoResponse;
import buscompany.model.Bus;
import buscompany.model.Order;
import buscompany.model.Passenger;
import buscompany.model.Trip;
import buscompany.model.TripDate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-09T17:24:21+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order orderDtoRequestToOrder(OrderDtoRequest orderDtoRequest, Trip trip) {
        if ( orderDtoRequest == null && trip == null ) {
            return null;
        }

        Order order = new Order();

        if ( orderDtoRequest != null ) {
            order.setTripDate( orderDtoRequestToTripDate( orderDtoRequest ) );
            order.setPassengers( passengerDtoRequestListToPassengerList( orderDtoRequest.getPassengers() ) );
        }
        if ( trip != null ) {
            order.setBusName( tripBusBusName( trip ) );
            order.setFromStation( trip.getFromStation() );
            order.setToStation( trip.getToStation() );
            order.setStart( trip.getStart() );
            order.setDuration( trip.getDuration() );
            order.setPrice( trip.getPrice() );
        }
        order.setTotalPrice( trip.getPrice() * orderDtoRequest.getPassengers().size() );
        order.setId( 0 );

        return order;
    }

    @Override
    public OrderDtoResponse orderToOrderDtoResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDtoResponse orderDtoResponse = new OrderDtoResponse();

        orderDtoResponse.setDate( orderTripDateDate( order ) );
        orderDtoResponse.setTripID( orderTripDateTripID( order ) );
        orderDtoResponse.setId( order.getId() );
        orderDtoResponse.setFromStation( order.getFromStation() );
        orderDtoResponse.setToStation( order.getToStation() );
        orderDtoResponse.setBusName( order.getBusName() );
        orderDtoResponse.setStart( order.getStart() );
        orderDtoResponse.setDuration( order.getDuration() );
        orderDtoResponse.setPrice( order.getPrice() );
        orderDtoResponse.setTotalPrice( order.getTotalPrice() );
        orderDtoResponse.setPassengers( passengerListToPassengerDtoResponseList( order.getPassengers() ) );

        return orderDtoResponse;
    }

    @Override
    public List<OrderDtoResponse> orderListToOrderDtoResponseList(List<Order> orderList) {
        if ( orderList == null ) {
            return null;
        }

        List<OrderDtoResponse> list = new ArrayList<OrderDtoResponse>( orderList.size() );
        for ( Order order : orderList ) {
            list.add( orderToOrderDtoResponse( order ) );
        }

        return list;
    }

    protected TripDate orderDtoRequestToTripDate(OrderDtoRequest orderDtoRequest) {
        if ( orderDtoRequest == null ) {
            return null;
        }

        TripDate tripDate = new TripDate();

        tripDate.setTripID( orderDtoRequest.getTripID() );
        if ( orderDtoRequest.getDate() != null ) {
            tripDate.setDate( LocalDate.parse( orderDtoRequest.getDate() ) );
        }

        return tripDate;
    }

    private String tripBusBusName(Trip trip) {
        if ( trip == null ) {
            return null;
        }
        Bus bus = trip.getBus();
        if ( bus == null ) {
            return null;
        }
        String busName = bus.getBusName();
        if ( busName == null ) {
            return null;
        }
        return busName;
    }

    protected Passenger passengerDtoRequestToPassenger(PassengerDtoRequest passengerDtoRequest) {
        if ( passengerDtoRequest == null ) {
            return null;
        }

        Passenger passenger = new Passenger();

        passenger.setFirstName( passengerDtoRequest.getFirstName() );
        passenger.setLastName( passengerDtoRequest.getLastName() );
        passenger.setPassport( passengerDtoRequest.getPassport() );

        return passenger;
    }

    protected List<Passenger> passengerDtoRequestListToPassengerList(List<PassengerDtoRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<Passenger> list1 = new ArrayList<Passenger>( list.size() );
        for ( PassengerDtoRequest passengerDtoRequest : list ) {
            list1.add( passengerDtoRequestToPassenger( passengerDtoRequest ) );
        }

        return list1;
    }

    private LocalDate orderTripDateDate(Order order) {
        if ( order == null ) {
            return null;
        }
        TripDate tripDate = order.getTripDate();
        if ( tripDate == null ) {
            return null;
        }
        LocalDate date = tripDate.getDate();
        if ( date == null ) {
            return null;
        }
        return date;
    }

    private int orderTripDateTripID(Order order) {
        if ( order == null ) {
            return 0;
        }
        TripDate tripDate = order.getTripDate();
        if ( tripDate == null ) {
            return 0;
        }
        int tripID = tripDate.getTripID();
        return tripID;
    }

    protected PassengerDtoResponse passengerToPassengerDtoResponse(Passenger passenger) {
        if ( passenger == null ) {
            return null;
        }

        PassengerDtoResponse passengerDtoResponse = new PassengerDtoResponse();

        passengerDtoResponse.setFirstName( passenger.getFirstName() );
        passengerDtoResponse.setLastName( passenger.getLastName() );
        passengerDtoResponse.setPassport( passenger.getPassport() );

        return passengerDtoResponse;
    }

    protected List<PassengerDtoResponse> passengerListToPassengerDtoResponseList(List<Passenger> list) {
        if ( list == null ) {
            return null;
        }

        List<PassengerDtoResponse> list1 = new ArrayList<PassengerDtoResponse>( list.size() );
        for ( Passenger passenger : list ) {
            list1.add( passengerToPassengerDtoResponse( passenger ) );
        }

        return list1;
    }
}
