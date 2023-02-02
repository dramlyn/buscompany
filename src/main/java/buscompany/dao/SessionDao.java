package buscompany.dao;

import buscompany.model.UserType;

import java.time.LocalDateTime;

public interface SessionDao {

    void initSession(int userId, String sessionID, LocalDateTime lastAccessTime);

    void logout(String sessionId);

    void leave(int userID);

    int getUserIdBySessionId(String sessionId);

    LocalDateTime getLastAccessTimeBySessionID(String sessionID);

    void updateLastAccessTimeBySessionID(String sessionID, LocalDateTime newAccessTime);


}
