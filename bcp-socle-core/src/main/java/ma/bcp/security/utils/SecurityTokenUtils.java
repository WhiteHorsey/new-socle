package ma.bcp.security.utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import ma.bcp.exception.custom.exceptions.UnTrustedIssuer;
import ma.bcp.security.config.AppSecretKeysConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class SecurityTokenUtils {

    @Value("${keycloak.allowedIssuers}")
    private String[] keycloakAllowedIssuers;
    @Autowired
    private AppSecretKeysConfigProperties appSecretKeysConfigProperties;

    public String extractTokenFromHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        return StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ") ? headerAuth.substring(7) : null;
    }

    private String getKeycloakCertificateUrl(DecodedJWT token) {
        return token.getIssuer() + "/protocol/openid-connect/certs";
    }

    private RSAPublicKey loadPublicKey(DecodedJWT token) throws JwkException {
        final String url = getKeycloakCertificateUrl(token);
        try {
            JwkProvider provider = new UrlJwkProvider(new URL(url));
            Jwk jwk = provider.get(token.getKeyId());
            if (jwk != null) {
                return (RSAPublicKey) jwk.getPublicKey();
            } else {
                throw new JwkException("JWK with the specified key ID not found.");
            }
        } catch (IOException e) {
            throw new JwkException("Failed to fetch JWK from the URL: " + url, e);
        }
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException, JWTCreationException, JwkException {
            DecodedJWT jwt = JWT.decode(token);
            Algorithm algorithm = chooseAlgorithmForToken(jwt);
            JWTVerifier verifier = buildVerifier(algorithm, jwt);
            return verifier.verify(token);
    }

    private Algorithm chooseAlgorithmForToken(DecodedJWT jwt) throws JwkException {
        if (isKeycloakIssuer(keycloakAllowedIssuers, jwt.getIssuer())) {
            RSAPublicKey publicKey = loadPublicKey(jwt);
            return Algorithm.RSA256(publicKey, null);
        } else if (isTrustedIssuer(jwt.getIssuer())) {
            return Algorithm.HMAC256(getAssociatedSecretKey(jwt.getIssuer()));
        } else {
            throw new UnTrustedIssuer("JWT validation failed: Issuer is not trusted!");
        }
    }

    private JWTVerifier buildVerifier(Algorithm algorithm, DecodedJWT jwt) {
        return JWT.require(algorithm).withIssuer(jwt.getIssuer()).build();
    }

    private boolean isKeycloakIssuer(String[] keycloakAllowedIssuers, String possibleIssuer) {
        return Arrays.asList(keycloakAllowedIssuers).contains(possibleIssuer);
    }

    private boolean isTrustedIssuer(String possibleIssuer) {
        return appSecretKeysConfigProperties.getApps().stream().anyMatch(app -> app.getName().equals(possibleIssuer));
    }

    private String getAssociatedSecretKey(String possibleIssuer) {
        Optional<AppSecretKeysConfigProperties.AppSecretKeys> app = appSecretKeysConfigProperties.getApps()
                .stream()
                .filter(x -> x.getName().equals(possibleIssuer))
                .findFirst();
        return app.map(AppSecretKeysConfigProperties.AppSecretKeys::getSecretKey).orElse(null);
    }

}