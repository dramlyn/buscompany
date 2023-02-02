package buscompany.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Service
public class AppPropertiesUtils {

    private int serverPort;
    private int maxNameLength;
    private int minPasswordLength;
    private int userIdleTimeout;

    @Autowired
    public AppPropertiesUtils(@Value("${server.port}") int serverPort,
                              @Value("${max_name_length}") int maxNameLength,
                              @Value("${min_password_length}") int minPasswordLength,
                              @Value("${user_idle_timeout}") int userIdleTimeout){
        this.serverPort = serverPort;
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
        this.userIdleTimeout = userIdleTimeout;
    }
}
