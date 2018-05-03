package eu.giuseppeurso.spring.mvc.security.cas.client.config;


import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;



/**
 * This class aims to centralize all the configuration properties of the CAS Server.
 * 
 * @author www.giuseppeurso.eu
 * 
 * @see http://www.baeldung.com/spring-security-cas-sso
 * @see https://docs.spring.io/spring-security/site/docs/current/reference/html/cas.html
 * @see https://gist.github.com/JacobASeverson/87de9ec12f78eebb62ca#file-websecurityconfig
 * @see https://github.com/cas-projects/cas-sample-java-webapp
 * @see https://github.com/apereo/java-cas-client
 *
 */
@Configuration
public class CasConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(CasConfigurer.class);

	/**
	 * The CAS global properties.
	 * @return
	 */
	@Bean
	public ServiceProperties serviceProperties() {
		String appLogin = "http://localhost:18080/mvc-casclient/login-cas";
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(appLogin);
		serviceProperties.setAuthenticateAllArtifacts(true);
		logger.info("Configured CAS Service Url (client login url): "+serviceProperties.getService());
		return serviceProperties;
	}

	/**
	 * The entry point of Spring Security authentication process (based on CAS).
	 * The user's browser will be redirected to the CAS login page.
	 * @return
	 */
	@Bean
	public AuthenticationEntryPoint casAuthenticationEntryPoint() {
		String casLogin = "http://localhost:8080/cas/login";
		CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
		entryPoint.setLoginUrl(casLogin);
		entryPoint.setServiceProperties(serviceProperties());
		logger.info("Configured CAS Login Url: "+entryPoint.getLoginUrl());
		return entryPoint;
	}

	/**
	 * CAS ticket validator, if you plan to use CAS 3.0 protocol
	 * @return
	 */
	@Bean
	public Cas30ServiceTicketValidator ticketValidatorCas30() {
		Cas30ServiceTicketValidator ticketValidator = new Cas30ServiceTicketValidator("http://localhost:8080/cas");
		return ticketValidator;
	}

	/**
	 * CAS ticket validator, if you plan to use CAS 2.0 protocol
	 * @return
	 */
	@Bean
	public Cas20ServiceTicketValidator ticketValidatorCas20() {
		return new Cas20ServiceTicketValidator("http://localhost:8080/cas");
	}

	
	/**
	 * The authentication provider that integrates with CAS.
	 * This implementation uses CAS 3.0 protocol for tickets validation. 
	 * 
	 * @see https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/cas/authentication/CasAuthenticationProvider.html
	 * @return
	 */
	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(serviceProperties());
		provider.setTicketValidator(ticketValidatorCas30());
		
		// Loads only a default set of authorities for any authenticated users (username and password are)
		//provider.setUserDetailsService((UserDetailsService) fakeUserDetailsService());
		
		// Loads additional user details using CasUserDetailsService (attribute resolution strategies need to be configured on the CAS Server)
		provider.setAuthenticationUserDetailsService(casUserDetailsService());
		
		provider.setKey("CAS_PROVIDER_KEY_LOCALHOST");
		
		return provider;
	}
		
	/**
	 * A custom UserDetailsService to load additional user details once the ticket validation request has been processed.
	 * 
	 * @see https://apereo.github.io/2018/02/20/cas-service-rbac-attributeresolution/
	 * @see https://apereo.github.io/cas/5.2.x/integration/Attribute-Resolution.html
	 */
	@Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> casUserDetailsService() {
        return new CasUserDetailsService();
    }

	/**
	 * CAS Authentication Provider does not use credentials specified here for authentication. It only loads
	 * the authorities for a user, once they have been authenticated by CAS.
	 * 
	 */
	@Bean
    public User fakeUserDetailsService(){
        return new User("fakeUser", "fakePass", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
	
	
}
