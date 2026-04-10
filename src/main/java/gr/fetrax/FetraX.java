package gr.fetrax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class FetraX {
    public static void main(String[] args) {
        SpringApplication.run(FetraX.class, args);
    }
}
