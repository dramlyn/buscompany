package buscompany.daoimpl;

import buscompany.dao.AdminDao;
import buscompany.dto.request.UpdateAdminDtoRequest;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.model.Admin;
import buscompany.model.UserType;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDaoImpl extends DaoImplBase implements AdminDao  {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);


    @Override
    public Admin insert(Admin admin) {
        LOGGER.info("DAO insert admin {}", admin);
        try(SqlSession sqlSession = getSession()){
            try{
                getUserMapper(sqlSession).insertUser(admin, UserType.ADMIN);
                getAdminMapper(sqlSession).insertAdmin(admin);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't insert admin {}, {}", admin, ex);
                sqlSession.rollback();
                Throwable cause = ex.getCause();
                if(cause instanceof MySQLIntegrityConstraintViolationException){
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "login", "Duplicate login");
                }
                throw ex;
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public void update(Admin updateAdmin, int userId) {
        LOGGER.debug("DAO update admin {}", updateAdmin);
        try(SqlSession sqlSession = getSession()){
            try {
                getUserMapper(sqlSession).updateUser(updateAdmin, userId);
                getAdminMapper(sqlSession).updateAdmin(updateAdmin.getPosition(), userId);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't update admin admin");
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public Admin getAdminById(int adminId) {
        LOGGER.debug("DAO get admin by id {}", adminId);
        try(SqlSession sqlSession = getSession()){
            try {
                return getAdminMapper(sqlSession).getAdminById(adminId);
            } catch (RuntimeException ex){
                LOGGER.debug("Can't get admin by id {}, {}", adminId, ex);
                throw ex;
            }
        }
    }



}
