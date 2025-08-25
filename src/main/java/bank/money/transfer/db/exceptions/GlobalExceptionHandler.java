package bank.money.transfer.db.exceptions;

import bank.money.transfer.util.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormat(InvalidFormatException ex) {
        Class<?> targetType = ex.getTargetType();
        String message;
        if (targetType != null) {
            if (targetType.getSimpleName().equals("Currency")) {
                String availableCurrencies = Arrays.stream(Currency.values())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));
                message = "Invalid currency type: " + ex.getValue() + "\nChoose one of the following types: " + availableCurrencies;
            } else {
                message = "Invalid value for type " + targetType.getSimpleName() + ": " + ex.getValue();
            }
        } else {
            message = "Invalid value: " + ex.getValue();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}