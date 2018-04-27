package eu.giuseppeurso.spring.mvc.security.cas.client.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * This class enables Spring Security and allows customization of the web based security.
 * It includes multiple authentication providers, which are queried in the order they’re declared (see the use of AuthenticationManagerBuilder).
 * In order to restricts access to specific http requests, the HttpSecurity is configured.
 * 
 * @author www.giuseppeurso.eu
 * 
 * @see https://docs.spring.io/spring-security/site/docs/current/reference/html5/#getting-started
 * @see https://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html
 *
 * @see http://www.baeldung.com/spring-security-cas-sso
 * @see https://objectpartners.com/2014/05/20/configuring-spring-security-cas-providers-with-java-config/
 * 
 * @see https://docs.spring.io/spring-security/site/docs/current/reference/html/cas.html
 * @see https://github.com/cas-projects/cas-sample-java-webapp
 * @see https://github.com/apereo/java-cas-client
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfigurer.class);
	
	// Let Spring resolve and inject collaborating beans into this class by @Autowired annotations...
	@Autowired
	private AuthenticationProvider casAuthenticationProvider;
    @Autowired
	private AuthenticationEntryPoint casAuthenticationEntryPoint;
    @Autowired
    private ServiceProperties casServiceProperties;

    
    /**
     * Configures web based security for specific http requests.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	logger.info("Configuring HttpSecurity mappers...");
    	 http
    	 	.csrf().disable()
    	 	.authorizeRequests().anyRequest().authenticated()
    	 	.and()
    	 	.httpBasic().authenticationEntryPoint(casAuthenticationEntryPoint)
    	    .and()
    	    .addFilter(casAuthenticationFilter());
    }
	
    /**
	 * Configures multiple Authentication providers. 
	 * AuthenticationManagerBuilder allows for easily building multiple authentication mechanisms in the order they're declared.
	 * CasAuthenticationProvider is used here.
	 */
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		boolean inMemoryAuthEnabled = false;
		logger.info("[IN-MEMORY] Authentication Provider enabled: "+inMemoryAuthEnabled);
		if (inMemoryAuthEnabled) {
			auth.inMemoryAuthentication().withUser("user").password("12345").roles("ROLE_USER");				
		}
		boolean casAuthEnabled = true;
		logger.info("[CAS] Authentication Provider enabled: "+casAuthEnabled);
		if (casAuthEnabled) {
			auth.authenticationProvider(casAuthenticationProvider);				
		}		
    }

	/**
	 * Cas authentication filter responsible processing a CAS service ticket.
	 * @return
	 * @throws Exception
	 */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(casServiceProperties);
        filter.setAuthenticationManager(authenticationManager());
        logger.info("Registered CAS Filter: "+filter.getClass().getSimpleName());
        return filter;
    }


    
}

