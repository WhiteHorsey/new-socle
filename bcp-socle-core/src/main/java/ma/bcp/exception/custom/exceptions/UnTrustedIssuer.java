package ma.bcp.exception.custom.exceptions;

public class UnTrustedIssuer extends RuntimeException{

    public UnTrustedIssuer() {
    }

    public UnTrustedIssuer(String message) {
        super(message);
    }

    public UnTrustedIssuer(String message, Throwable cause) {
        super(message, cause);
    }

    public UnTrustedIssuer(Throwable cause) {
        super(cause);
    }

    public UnTrustedIssuer(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}