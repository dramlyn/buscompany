package buscompany.daoimpl;

import buscompany.dao.SessionDao;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@NoArgsConstructor
public class SessionDaoImpl extends DaoImplBase implements SessionDao {


    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);

    @Override
    public void initSession(int userID, String sessionID, LocalDateTime lastAccessTime) {
        LOGGER.debug("DAO initialize session for user id {}", userID);
        try(SqlSession sqlSession = getSession()){
            try{
                getSessionMapper(sqlSession).initSession(userID, sessionID, lastAccessTime);
            } catch (RuntimeException ex){
                Throwable cause = ex.getCause();
                if(cause instanceof MySQLIntegrityConstraintViolationException){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "userID", "User already logged in.");
                }
                LOGGER.debug("Can't initialize session for user {}", userID);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void logout(String sessionID) {
        LOGGER.debug("DAO logout user with sessionID = {}", sessionID);
        try(SqlSession sqlSession = getSession()){
            try {
                getSessionMapper(sqlSession).logout(sessionID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't logout user");
                sqlSession.rollback();
                Throwable cause = ex.getCause();
                if(cause instanceof MySQLIntegrityConstraintViolationException){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "sessionID", "This sessionID doesn't exist");
                }
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void leave(int userID) {
        LOGGER.debug("DAO leave user with userID = {}", userID);
        try(SqlSession sqlSession = getSession()){
            try {
                getSessionMapper(sqlSession).leave(userID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't leave user");
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getUserIdBySessionId(String sessionID) {
        LOGGER.debug("DAO get userId by sessionID {}", sessionID);
        try(SqlSession sqlSession = getSession()){
            try{
                Optional<Integer> userID = Optional.ofNullable(getSessionMapper(sqlSession).getUserIdBySessionId(sessionID));
                if(userID.isEmpty()){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "sessionID", "This sessionID doesn't exist.");
                }
                else return userID.get();

            } catch (RuntimeException ex){
                LOGGER.debug("Can't get userId by sessionID {}, {}", sessionID, ex);
                throw ex;
            }
        }
    }

    @Override
    public LocalDateTime getLastAccessTimeBySessionID(String sessionID) {
        LOGGER.debug("DAO get last access time by sessionID = {}", sessionID);
        try(SqlSession sqlSession = getSession()){
            try {
                return getSessionMapper(sqlSession).getLastAccessTimeBySessionID(sessionID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get last access time by sessionID = {}, {}", sessionID, ex);
                Throwable cause = ex.getCause();
                if(cause instanceof MySQLIntegrityConstraintViolationException){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "sessionID", "This sessionID doesn't exist.");
                }
                throw ex;
            }
        }
    }

    @Override
    public void updateLastAccessTimeBySessionID(String sessionID, LocalDateTime newAccessTime) {
        LOGGER.debug("DAO update last access time by sessionID = {}", sessionID);
        try(SqlSession sqlSession = getSession()){
            try {
                getSessionMapper(sqlSession).updateLastAccessTimeBySessionID(newAccessTime, sessionID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't update last access time by sessionID = {}, {}", sessionID, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }




}
