package ma.bcp.exception.custom.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class CustomJwtValidationException extends RuntimeException {
    public CustomJwtValidationException(String message) {
        super(message);
    }
}