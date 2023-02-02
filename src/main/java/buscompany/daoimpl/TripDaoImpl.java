package buscompany.daoimpl;

import buscompany.dao.TripDao;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.mappers.TripMapper;
import buscompany.model.Trip;
import buscompany.model.TripDate;
import com.mysql.jdbc.MysqlDataTruncation;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.List;
@Repository
public class TripDaoImpl extends DaoImplBase implements TripDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripDaoImpl.class);

    @Override
    public Trip addTrip(Trip trip) {
        LOGGER.debug("DAO add trip {}", trip);
        try (SqlSession sqlSession = getSession()) {
            try {
                TripMapper tripMapper = getTripMapper(sqlSession);

                tripMapper.insertTrip(trip);
                int tripID = trip.getId();

                tripMapper.insertTripDates(trip.getDates(), tripID);
                for(TripDate tripDate : trip.getDates()){
                    for(int i = 1; i <= tripDate.getFreePlaces(); i++){
                        getPlacesMapper(sqlSession).insertPlaceByDate(tripDate.getDate(), tripID, i);
                    }
                }
                if (trip.getSchedule() != null) {
                    tripMapper.insertSchedule(tripID, trip.getSchedule());
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add trip {}, {}", trip, ex);
                sqlSession.rollback();
                Throwable cause = ex.getCause();
                if(cause instanceof MysqlDataTruncation || cause instanceof DateTimeParseException){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "dates", "You entered wrong dates.");
                }
                throw ex;
            }
            sqlSession.commit();
        }
        return trip;
    }

    @Override
    public Trip selectTripById(int tripId) {
        LOGGER.debug("DAO select trip by id {}", tripId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getTripMapper(sqlSession).selectTripByTripID(tripId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't select trip by id {}, {}", tripId, ex);
            }
        }
        return new Trip();
    }

    @Override
    public void updateTrip(Trip trip) {
        LOGGER.debug("DAO change trip {}", trip);
        try (SqlSession sqlSession = getSession()) {
            try {
                TripMapper tripMapper = getTripMapper(sqlSession);
                int tripID = trip.getId();

                tripMapper.updateTrip(trip);
                if(trip.getDates() != null || trip.getSchedule() != null) {
                    tripMapper.deleteDatesById(tripID);
                    tripMapper.deleteScheduleByID(tripID);
                    tripMapper.insertTripDates(trip.getDates(), tripID);
                    for(TripDate tripDate : trip.getDates()){
                        for(int i = 1; i <= tripDate.getFreePlaces(); i++){
                            getPlacesMapper(sqlSession).insertPlaceByDate(tripDate.getDate(), tripID, i);
                        }
                    }
                    if (trip.getSchedule() != null) {
                        tripMapper.insertSchedule(tripID, trip.getSchedule());
                    }
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't change trip {}, {}", trip, ex);
                sqlSession.rollback();
                Throwable cause = ex.getCause();
                if(cause instanceof MysqlDataTruncation){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "dates", "You entered wrong dates.");
                }
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteTrip(int tripID) {
        LOGGER.debug("DAO delete trip with ID = {}", tripID);
        try (SqlSession sqlSession = getSession()) {
            try {
                getTripMapper(sqlSession).deleteTrip(tripID);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't delete trip with ID = {}, {}", tripID, ex);
                sqlSession.rollback();
            }
            sqlSession.commit();
        }
    }

    @Override
    public void approveTrip(int tripID) {
        LOGGER.debug("DAO approve trip with ID = {}", tripID);
        try(SqlSession sqlSession = getSession()){
            try {
                getTripMapper(sqlSession).approveTrip(tripID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't approve trip with ID = {}, {}", tripID, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Trip> getTripList(String fromStation, String toStation, String busName, boolean approved) {
        LOGGER.debug("DAO get trip list");
        try(SqlSession sqlSession = getSession()){
            try {
                return getTripMapper(sqlSession).selectTripList(fromStation, toStation, busName, approved);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get trip list");
                throw ex;
            }
        }
    }
}
