package cn.shenjunjie.booking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
/**
 * @author JunjieShen
 */
@ComponentScan("cn.shenjunjie")
@MapperScan("cn.shenjunjie.booking.dao")
@EnableAsync
@EnableWebMvc
@SpringBootApplication
public class BookingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingPlatformApplication.class, args);
    }

}
