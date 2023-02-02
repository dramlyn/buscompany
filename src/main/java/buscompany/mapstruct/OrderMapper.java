package buscompany.mapstruct;

import buscompany.dto.request.OrderDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.model.Order;
import buscompany.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "totalPrice", expression = "java(trip.getPrice() * orderDtoRequest.getPassengers().size())")
    @Mapping(target = "id", constant = "0")
    @Mapping(source = "trip.bus.busName", target = "busName")
    @Mapping(source = "orderDtoRequest.tripID", target = "tripDate.tripID")
    @Mapping(source = "orderDtoRequest.date", target = "tripDate.date")
    Order orderDtoRequestToOrder(OrderDtoRequest orderDtoRequest, Trip trip);

    @Mapping(source = "order.tripDate.date", target = "date")
    @Mapping(source = "order.tripDate.tripID", target = "tripID")
    OrderDtoResponse orderToOrderDtoResponse(Order order);

    List<OrderDtoResponse> orderListToOrderDtoResponseList(List<Order> orderList);
}
