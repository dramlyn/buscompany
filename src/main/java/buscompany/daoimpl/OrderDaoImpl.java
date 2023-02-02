package buscompany.daoimpl;

import buscompany.dao.OrderDao;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.model.Order;
import buscompany.model.Passenger;
import buscompany.model.TripDate;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderDaoImpl extends DaoImplBase implements OrderDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Override
    public Order insertOrder(Order order, int clientID) {
        LOGGER.debug("DAO insert order {}", order);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Passenger> passengerList = order.getPassengers();
                TripDate tripDate = order.getTripDate();
                int updates = getPlacesMapper(sqlSession).updateFreePlaces(passengerList.size(), tripDate.getTripID(), tripDate.getDate());
                if(updates == 0){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "passengers", "Not enough space for passengers.");
                }
                getOrderMapper(sqlSession).insertOrder(order, clientID);
                getOrderMapper(sqlSession).insertPassengers(passengerList, order.getId());
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't insert order {}, {}", order, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return order;
    }

    @Override
    public List<Order> getOrderList(String fromStation, String toStation, String busName, int clientID) {
        LOGGER.debug("DAO get order list");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).selectOrderList(fromStation, toStation, busName, clientID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get order list");
                throw ex;
            }
        }
    }

    @Override
    public Order getOrderByOrderID(int orderID) {
        LOGGER.debug("DAO get order by order ID {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).selectOrderByOrderID(orderID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get order by order ID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public void deleteOrder(int orderID) {
        LOGGER.debug("DAO delete order with id {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                getOrderMapper(sqlSession).deleteOrder(orderID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't delete order with id {}, {}", orderID, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getTripIDByOrderID(int orderID) {
        LOGGER.debug("DAO get tripID with orderID {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).selectTripIDByOrderID(orderID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get tripID with orderID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public LocalDate getOrderDateByOrderID(int orderID) {
        LOGGER.debug("DAO get order date with orderID {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).selectOrderDateByOrderID(orderID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get order date with orderID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public int checkOrderByOrderID(int orderID) {
        LOGGER.debug("DAO check order by orderID {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).checkOrderByOrderID(orderID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't check order by orderID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public String getBusNameByOrderID(int orderID) {
        LOGGER.debug("DAO get bus name by orderID {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).selectBusNameByOrderID(orderID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get bus name by orderID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public int checkClientIDInOrder(int orderID, int clientID) {
        LOGGER.debug("DAO check clientID in order by orderID {}", orderID);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).checkClientIDInOrder(orderID, clientID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't check clientID in order by orderID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getPassengerIDByOrderIDAndPassport(int orderID, String passport) {
        LOGGER.debug("DAO get passengerID by orderID {} and passport {}", orderID, passport);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getOrderMapper(sqlSession).selectPassengerIDByOrderIDAndPassport(orderID, passport);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get passengerID by orderID {} and passport {}, {}", orderID, passport, ex);
                throw ex;
            }
        }
    }
}
