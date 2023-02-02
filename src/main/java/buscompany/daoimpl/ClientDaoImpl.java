package buscompany.daoimpl;

import buscompany.dao.ClientDao;
import buscompany.dto.request.UpdateClientDtoRequest;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import buscompany.model.Client;
import buscompany.model.UserType;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDaoImpl extends DaoImplBase implements ClientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDaoImpl.class);

    @Override
    public Client insert(Client client) {
        LOGGER.debug("DAO insert client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(client, UserType.CLIENT);
                getClientMapper(sqlSession).insertClient(client);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't insert client {}, {}", client, ex);
                sqlSession.rollback();
                Throwable cause = ex.getCause();
                if (cause instanceof MySQLIntegrityConstraintViolationException) {
                    throw new ServerException(ServerErrorCode.DATABASE_ERROR, "login", "This login is already taken.");
                }
                throw ex;
            }
            sqlSession.commit();
        }
        return client;
    }

    @Override
    public List<Client> getAllClients() {
        LOGGER.debug("DAO get all clients");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getClientMapper(sqlSession).getAllClients();
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't get all clients ");
                throw ex;
            }
        }
    }

    @Override
    public Client getClientById(int clientId) {
        LOGGER.debug("DAO get client by id {}", clientId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getClientMapper(sqlSession).getClientById(clientId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get client with id {}, {}", clientId, ex);
                throw ex;
            }
        }
    }

    @Override
    public void update(Client updateClient, int clientId) {
        LOGGER.debug("DAO update client to params {} with id {}", updateClient, clientId);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).updateUser(updateClient, clientId);
                getClientMapper(sqlSession).updateClient(updateClient.getEmail(), updateClient.getPhone(), clientId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update client to params {} with id {}, {}", updateClient, clientId, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
