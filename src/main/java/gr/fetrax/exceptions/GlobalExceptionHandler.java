package gr.fetrax.exceptions;

import gr.fetrax.util.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

        // Log the bad request so it appears in Loki
        log.warn("Bad request - {}", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}