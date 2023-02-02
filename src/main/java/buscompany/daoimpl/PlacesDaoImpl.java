package buscompany.daoimpl;

import buscompany.dao.PlacesDao;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.model.Place;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PlacesDaoImpl extends DaoImplBase implements PlacesDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlacesDaoImpl.class);

    @Override
    public List<Integer> getFreePlacesList(int tripID, LocalDate date) {
        LOGGER.debug("DAO get occupied places for trip with id {} and date {}", tripID, date);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).selectFreePlacesList(tripID, date);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get occupied places for trip with id {} and date {}, {}", tripID, date, ex);
                throw ex;
            }
        }
    }

    @Override
    public int insertPlace(Place place, int passengerID) {
        LOGGER.debug("DAO insert place {} with passengerID {}", place, passengerID);
        int updates = 0;
        try(SqlSession sqlSession = getSession()){
            try {
                updates = getPlacesMapper(sqlSession).choosePlace(place, passengerID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't insert place {} with passengerID {}, {}", place, passengerID, ex);
                sqlSession.rollback();
                Throwable cause = ex.getCause();
                if(cause instanceof MySQLIntegrityConstraintViolationException){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "place", "This place is occupied.");
                }
                throw ex;
            }
            sqlSession.commit();
        }
        return updates;
    }

    @Override
    public int getFreePlaces(int tripID, LocalDate date) {
        LOGGER.debug("DAO get occupied places with tripID {} and date {}", tripID, date);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).getFreePlaces(tripID, date);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get occupied places with tripID {} and date {}, {}", tripID, date, ex);
                throw ex;
            }
        }

    }


    @Override
    public int isPlacedByThisPassenger(int orderID, int passengerID) {
        LOGGER.debug("DAO check passenger is placed with orderID {} and passengerID {}", orderID, passengerID);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).isPlaced(orderID, passengerID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't check passenger is placed with orderID {} and passengerID {}, {}", orderID, passengerID, ex);
                throw ex;
            }
        }
    }

    @Override
    public void deletePassengerPlace(int orderID, int passengerID) {
        LOGGER.debug("DAO delete placed passenger with orderID {} and passengerID {}", orderID, passengerID);
        try(SqlSession sqlSession = getSession()){
            try {
                getPlacesMapper(sqlSession).deletePassengerPlace(orderID, passengerID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't delete placed passenger with orderID {} and passengerID {}, {}", orderID, passengerID, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int checkPassenger(int orderID, String passport) {
        LOGGER.debug("DAO check passenger with orderID {} and passport {}", orderID, passport);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).checkPassenger(orderID, passport);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't check passenger with orderID {} and passport {}, {}", orderID, passport, ex);
                throw ex;
            }
        }
    }

    @Override
    public int checkPlacedByAnotherPassenger(int place, int tripID, LocalDate date) {
        LOGGER.debug("DAO check placed by another passenger by tripID {}, date {} and place {}", tripID, date, place);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).checkPlacedByAnotherPassenger(date, tripID, place);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't check placed by another passenger by tripID {}, date {} and place {}, {}", tripID, date, place, ex);
                throw ex;
            }
        }
    }

    @Override
    public int getOccupiedPlacesByOrder(int orderID) {
        LOGGER.debug("DAO get occupied places by order with orderID {}", orderID);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).selectPlacedPassengersByOrderID(orderID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get occupied places by order with orderID {}, {}", orderID, ex);
                throw ex;
            }
        }
    }

    @Override
    public void updateFreePlaces(int newOccupiedPlaces, int tripID, LocalDate date) {
        LOGGER.debug("DAO update occupied places by tripID {}, date {} and newOccupiedPlaces {}", tripID, date, newOccupiedPlaces);
        try(SqlSession sqlSession = getSession()){
            try {
                getPlacesMapper(sqlSession).updateFreePlaces(newOccupiedPlaces, tripID, date);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't update occupied places by tripID {}, date {} and newOccupiedPlaces {}, {}", tripID, date, newOccupiedPlaces, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteOrderPassengersPlaces(int orderID) {
        LOGGER.debug("DAO delete order passengers places by orderID {}", orderID);
        try(SqlSession sqlSession = getSession()){
            try {
                getPlacesMapper(sqlSession).deleteOrderPassengersPlaces(orderID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't delete order passengers places by orderID {}, {}", orderID, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int checkPassportFirstAndLastName(int orderID, String passport, String firstName, String lastName) {
        LOGGER.debug("DAO check passport firstName and lastName with orderID {}, passport {}, firstName {} and lastName {}", orderID, passport, firstName, lastName);
        try(SqlSession sqlSession = getSession()){
            try {
                return getPlacesMapper(sqlSession).checkPassportFirstAndLastName(passport, orderID, firstName, lastName);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't check passport firstName and lastName with orderID {}, passport {}, firstName {} and lastName {}, {}", orderID, passport, firstName, lastName, ex);
                throw ex;
            }
        }
    }
}
