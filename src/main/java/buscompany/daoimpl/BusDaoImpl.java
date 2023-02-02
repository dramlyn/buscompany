package buscompany.daoimpl;

import buscompany.dao.BusDao;
import buscompany.dto.response.BusDtoResponse;
import buscompany.model.Bus;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class BusDaoImpl extends DaoImplBase implements BusDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusDaoImpl.class);


    @Override
    public List<Bus> getAllBusBrands() {
        LOGGER.debug("DAO get all bus brands");
        try(SqlSession sqlSession = getSession()){
            try {
                return getBusMapper(sqlSession).getAllBusBrands();
            } catch (RuntimeException ex){
                LOGGER.info("Can't get all bus brands");
                throw ex;
            }
        }
    }

    @Override
    public Bus getBusByBusName(String busName) {
        LOGGER.debug("DAO get bus by bus name {}", busName);
        try(SqlSession sqlSession = getSession()){
            try {
                return getBusMapper(sqlSession).getBusByBusName(busName);
            } catch (RuntimeException ex){
                LOGGER.info("Can't get bus by bus name {}, {}", busName, ex);
                throw ex;
            }
        }
    }

    @Override
    public Integer getPlaceCountByBusName(String busName) {
        LOGGER.debug("DAO get place count by bus name {}", busName);
        try(SqlSession sqlSession = getSession()){
            try {
                return getBusMapper(sqlSession).getPlaceCountByBusName(busName);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get place count by bus name {}, {}", busName, ex);
                throw ex;
            }
        }
    }
}
