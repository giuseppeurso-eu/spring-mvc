package eu.giuseppeurso.spring.mvc.security.cas.client.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Allows for retrieving a Spring Security UserDetails object starting from a response to a ticket validation request (CasAssertionAuthenticationToken).
 * 
 * @author www.giuseppeurso.eu
 * @see https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/cas/authentication/CasAssertionAuthenticationToken.html
 *
 */
public class CasUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

	private static final Logger logger = LoggerFactory.getLogger(CasUserDetailsService.class);
	
	@Override
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
		
		AttributePrincipal principal = token.getAssertion().getPrincipal();
		String username = principal.getName();
		logger.info("Principal name:  "+username);
		Map<String, Object> attributes = principal.getAttributes();
		for(Map.Entry<String, Object> attribute : attributes.entrySet()) {
			logger.info("Principal attribute "+attribute.getKey()+"="+attribute.getValue());
		}
		
		// Forces a default user authority.
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		User loggedUser = new User(username, "", authorities);
		CasUser casUser = new CasUser();
		casUser.setUser(loggedUser);
		casUser.setAttributes(attributes);
		return casUser;
	}

}
