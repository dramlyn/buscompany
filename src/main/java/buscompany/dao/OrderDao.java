package buscompany.dao;

import buscompany.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    Order insertOrder(Order order, int clientID);

    List<Order> getOrderList(String fromStation, String toStation, String busName, int clientID);

    Order getOrderByOrderID(int orderID);

    void deleteOrder(int orderID);

    int getTripIDByOrderID(int orderID);

    LocalDate getOrderDateByOrderID(int orderID);

    int checkOrderByOrderID(int orderID);

    String getBusNameByOrderID(int orderID);

    int checkClientIDInOrder(int orderID, int clientID);

    int getPassengerIDByOrderIDAndPassport(int orderID, String passport);
}
