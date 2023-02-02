package buscompany.daoimpl;

import buscompany.dao.UserDao;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.model.User;
import buscompany.model.UserType;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        LOGGER.debug("DAO get user with login {} and password {}", login, password);
        try (SqlSession sqlSession = getSession()) {
            try {
                UserType userType = getUserMapper(sqlSession).selectUserTypeByLogin(login);
                if (userType == null) {
                    throw new ServerException(ServerErrorCode.WRONG_LOGIN, "login", "You entered wrong login.");
                }

                if (userType.equals(UserType.CLIENT)) {
                    User client = getClientMapper(sqlSession).getClientByLoginAndPassword(login, password);
                    if (client == null) {
                        throw new ServerException(ServerErrorCode.WRONG_PASSWORD, "password", "You entered wrong password.");
                    }
                    return client;
                } else {
                    User admin = getAdminMapper(sqlSession).getAdminByLoginAndPassword(login, password);
                    if(admin == null){
                        throw new ServerException(ServerErrorCode.WRONG_PASSWORD, "password", "You entered wrong password.");
                    }
                    return admin;
                }
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't login user with login {} and password {}, {}", login, password, ex);
                throw ex;
            }
        }
    }

    @Override
    public User getUserBySessionId(String sessionId) {
        LOGGER.debug("DAO get user by sessionId {}", sessionId);
        try (SqlSession sqlSession = getSession()) {
            try {
                int userId = getSessionMapper(sqlSession).getUserIdBySessionId(sessionId);
                UserType userType = getUserMapper(sqlSession).selectUserTypeById(userId);
                if (userType.equals(UserType.CLIENT)) {
                    return getClientMapper(sqlSession).getClientById(userId);
                } else {
                    return getAdminMapper(sqlSession).getAdminById(userId);
                }
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get user by sessionId {}, {}", sessionId, ex);
                throw ex;
            }
        }
    }

    @Override
    public UserType selectUserTypeById(int userId) {
        LOGGER.debug("DAO select user type by id {}", userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).selectUserTypeById(userId);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't select user type by id {}, {}", userId, ex);
                throw ex;
            }
        }
    }

    @Override
    public String selectPasswordByUserID(int userID) {
        LOGGER.debug("DAO select password by id {}", userID);
        try(SqlSession sqlSession = getSession()){
            try{
               return getUserMapper(sqlSession).selectPasswordByUserID(userID);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't select password by userID {}, {}", userID, ex);
                throw ex;
            }
        }
    }

}
