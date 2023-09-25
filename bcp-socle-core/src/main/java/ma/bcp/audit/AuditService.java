package ma.bcp.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Configuration
@EnableAsync
@Service
public class AuditService {

	@Autowired
	private AuditRepository auditRepository;

	@Async
	public void storeAuditActivity(AuditDTO auditDTO) {
		this.auditRepository.save(auditDTO);
	}

}