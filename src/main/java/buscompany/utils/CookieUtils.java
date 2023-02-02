package buscompany.utils;

import buscompany.daoimpl.SessionDaoImpl;
import buscompany.exception.ServerErrorCode;
import buscompany.exception.ServerException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CookieUtils {

    private SessionDaoImpl sessionDao;

    private AppPropertiesUtils appPropertiesUtils;


    public void setSession(HttpServletResponse servletResponse, int userId){
        String sessionID = UUID.randomUUID().toString();
        LocalDateTime lastAccessTime = LocalDateTime.now();
        sessionDao.initSession(userId, sessionID, lastAccessTime);
        servletResponse.addCookie(new Cookie("JAVASESSIONID", sessionID));
    }

    public int handleSessionID(String sessionID){
        int userID = sessionDao.getUserIdBySessionId(sessionID);

        LocalDateTime lastAccessTime = sessionDao.getLastAccessTimeBySessionID(sessionID);
        LocalDateTime currentAccessTime = LocalDateTime.now();

        if(ChronoUnit.SECONDS.between(lastAccessTime, currentAccessTime) >= appPropertiesUtils.getUserIdleTimeout()){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED, "JAVASESSIONID", "Session is not valid");
        }
        sessionDao.updateLastAccessTimeBySessionID(sessionID, currentAccessTime);
        return userID;
    }

}
