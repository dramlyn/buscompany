package buscompany.mappers;

import buscompany.model.Order;
import buscompany.model.Passenger;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

public interface OrderMapper {

    @Insert("INSERT INTO `order`(id_trip, id_client, fromStation, toStation, busName, `date`, `start`, duration, price, totalPrice) VALUES" +
            "(#{order.tripDate.tripID}, #{clientID}, #{order.fromStation}, #{order.toStation}, #{order.busName}, #{order.tripDate.date}, #{order.start}," +
            " #{order.duration}, #{order.price}, #{order.totalPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "order.id")
    Integer insertOrder(@Param("order") Order order, @Param("clientID") int clientID);

    @Insert({
            "<script>",
            "INSERT INTO `passenger`(id_order, firstName, lastName, passport) VALUES",
            "<foreach item='item' collection='passengers' separator=','>",
            "(#{orderID}, #{item.firstName}, #{item.lastName}, #{item.passport})",
            "</foreach>",
            "</script>"
    })
    void insertPassengers(@Param("passengers") List<Passenger> passengers, @Param("orderID") int orderID);

    @Select("SELECT firstName, lastName, passport FROM `passenger` WHERE id_order = #{orderID}")
    List<Passenger> selectPassengers(@Param("orderID") int orderID);

    List<Order> selectOrderList(@Param("fromStation") String fromStation,
                                @Param("toStation") String toStation,
                                @Param("busName") String busName,
                                @Param("clientID") int clientID);

    Order selectOrderByOrderID(@Param("orderID") int orderID);

    @Delete("DELETE FROM `order` WHERE id = #{orderID}")
    void deleteOrder(@Param("orderID") int orderID);

    @Select("SELECT id_trip FROM `order` WHERE id = #{orderID}")
    int selectTripIDByOrderID(@Param("orderID") int orderID);

    @Select("SELECT `date` FROM `order` WHERE id = #{orderID}")
    LocalDate selectOrderDateByOrderID(@Param("orderID") int orderID);

    @Select("SELECT count(*) FROM `order` WHERE id = #{orderID}")
    int checkOrderByOrderID(@Param("orderID") int orderID);

    @Select("SELECT busName FROM `order` WHERE id = #{orderID}")
    String selectBusNameByOrderID(@Param("orderID") int orderID);

    @Select("SELECT count(*) FROM `order` WHERE id = #{orderID} AND id_client = #{clientID}")
    int checkClientIDInOrder(@Param("orderID") int orderID, @Param("clientID") int clientID);

    @Select("SELECT id FROM `passenger` WHERE id_order = #{orderID} AND passport = #{passport}")
    int selectPassengerIDByOrderIDAndPassport(@Param("orderID") int orderID, @Param("passport") String passport);

}
