package buscompany.daoimpl;

import buscompany.dao.DebugDao;
import buscompany.model.Bus;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class DebugDaoImpl extends DaoImplBase implements DebugDao {
    @Override
    public void deleteDataBase() {
        log.debug("DAO delete database");
        try(SqlSession sqlSession = getSession()){
            try{
                getUserMapper(sqlSession).deleteAll();
                getTripMapper(sqlSession).deleteAll();
                getBusMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex){
                log.debug("Can't delete database");
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void insertBus(Bus bus) {
        log.debug("DAO insert bus {}", bus);
        try(SqlSession sqlSession = getSession()){
            try{
                getDebugMapper(sqlSession).insertBus(bus);
            } catch (RuntimeException ex){
                log.debug("Can't insert bus {}, {}", bus, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
