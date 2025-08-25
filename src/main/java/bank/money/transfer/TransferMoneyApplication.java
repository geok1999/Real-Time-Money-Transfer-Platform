package bank.money.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class TransferMoneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferMoneyApplication.class, args);
    }

}
