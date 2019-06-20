package club.lzlbog.chatting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author li
 * @version 1.0
 * @date 2019-3-17 09:50
 */
@SpringBootApplication
@EnableTransactionManagement
public class ChattingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingApplication.class, args);
    }

}
