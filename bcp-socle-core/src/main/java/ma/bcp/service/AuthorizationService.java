package ma.bcp.service;

import java.util.List;

/**
 * @author bilalslayki
 *
 */
public interface AuthorizationService {

	List<String> getAuthorizations(String userLogin);

}