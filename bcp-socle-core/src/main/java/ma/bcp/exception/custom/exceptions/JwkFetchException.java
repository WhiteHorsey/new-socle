package ma.bcp.exception.custom.exceptions;

import com.auth0.jwk.JwkException;

public class JwkFetchException extends RuntimeException {
    public JwkFetchException(String message, JwkException e) {
        super(message);
    }
}