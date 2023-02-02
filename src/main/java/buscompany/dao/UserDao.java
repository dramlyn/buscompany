package buscompany.dao;

import buscompany.model.User;
import buscompany.model.UserType;

public interface UserDao {
    User getUserByLoginAndPassword(String login, String password);

    User getUserBySessionId(String sessionID);

    UserType selectUserTypeById(int userID);

    String selectPasswordByUserID(int userID);
}
