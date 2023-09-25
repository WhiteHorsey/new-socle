package ma.bcp.client;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class CoreClient {
    public String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((DecodedJWT) authentication.getPrincipal()).getToken();
    }
}