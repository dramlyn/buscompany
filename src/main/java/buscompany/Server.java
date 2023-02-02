package buscompany;

import buscompany.utils.MyBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    private static Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args){
        MyBatisUtils.initSqlSessionFactory();
        LOGGER.info("Start");
        SpringApplication.run(Server.class);
    }

}
