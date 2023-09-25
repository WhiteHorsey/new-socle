package ma.bcp.audit;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * @author bilalslayki
 */
@Aspect
@Component
@Configuration
@EnableMongoRepositories("ma.bcp.audit")
public class AuditAspect {

    @Autowired
    private HttpServletRequest request;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private AuditService auditService;

    @After("@annotation(auditable)")
    @Transactional
    public void auditActivity(JoinPoint joinPoint, Auditable auditable) {

        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setAction(auditable.action());
        auditDTO.setApplication(this.applicationName);

        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication != null) {
            DecodedJWT jwt = (DecodedJWT) authentication.getPrincipal();

            if (!jwt.getClaim("clientId").isNull()) {
                auditDTO.setUser(jwt.getClaim("clientId").asString());
            } else if (!jwt.getClaim("preferred_username").isNull()) {
                auditDTO.setUser(jwt.getClaim("preferred_username").asString());
            } else {
                auditDTO.setUser(jwt.getClaim("sub").asString());
            }
        }

        // SET USER IP
        auditDTO.setUserIP(this.request.getRemoteAddr());

        // CURRENT TIMESTAMP
        Timestamp currentTime = new Timestamp(new java.util.Date().getTime());
        auditDTO.setDate(currentTime.toString());

        StringBuilder description = new StringBuilder();
        Object[] arguments = joinPoint.getArgs();
        if (arguments != null) {
            for (Object object : arguments) {
                if (object instanceof AuditableEntity) {
                    description.append(((AuditableEntity) object).getAuditInformation());
                }
            }
        }
        auditDTO.setDescription(description.toString());
        this.auditService.storeAuditActivity(auditDTO);
    }

}