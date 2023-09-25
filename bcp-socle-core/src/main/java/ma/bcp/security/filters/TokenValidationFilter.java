package ma.bcp.security.filters;

import com.auth0.jwk.JwkException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import ma.bcp.exception.handler.ApiError;
import ma.bcp.security.utils.SecurityTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class TokenValidationFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityTokenUtils securityTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = securityTokenUtils.extractTokenFromHeader(request);
            if (token != null) {
                DecodedJWT jwt = securityTokenUtils.validateToken(token);
                Authentication authentication = new UsernamePasswordAuthenticationToken(jwt, null, extractRolesFromToken(jwt));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JWTVerificationException | JwkException e) {
            log.error("Token validation failed: " + e.getMessage());
            setErrorResponse(response, "JWT validation Error", e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public void setErrorResponse(HttpServletResponse response, String message, Throwable ex) throws IOException {
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(new ApiError(UNAUTHORIZED, message, ex.getMessage()).convertToJson());
    }

    public static Collection<? extends GrantedAuthority> extractRolesFromToken(DecodedJWT decodedJWT) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return authorities;
    }
}