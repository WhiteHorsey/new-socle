package ma.bcp.exception.handler;


import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import ma.bcp.exception.custom.exceptions.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationExceptions(Exception ex) {
        return new ApiError(UNAUTHORIZED, "username or password are incorrect.", ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiError handleAccountStatusException(AccountStatusException ex) {
        return new ApiError(UNAUTHORIZED, "user account is abnormal (disable or locked).", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiError handleAccessDeniedException(AccessDeniedException ex) {
        return new ApiError(FORBIDDEN, "No permissions.", ex.getMessage());
    }

    @ExceptionHandler({CustomJwtValidationException.class, AuthenticationException.class,
            JwkFetchException.class, JWTCreationException.class, JWTVerificationException.class, JWTDecodeException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ApiError handleTokenValidationException(Exception ex) {
        return new ApiError(UNAUTHORIZED, "JWT validation Error.", ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError handleAccessDeniedException(NoHandlerFoundException ex) {
        return new ApiError(NOT_FOUND, "This API endpoint is not found.", ex.getMessage());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError customException(RuntimeException ex) {
        return new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(value = {InvalidURIException.class, ImageConversionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError customBadRequestException(RuntimeException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationException(MethodArgumentNotValidException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().get(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraint(ConstraintViolationException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiError handleOtherException(Exception ex) {
        return new ApiError(INTERNAL_SERVER_ERROR, "A server internal error occurs.", ex.getMessage());
    }

}