package eu.giuseppeurso.spring.mvc.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This class enables Spring Security and allows customization of the web based security.
 * It includes multiple authentication providers, which are queried in the order they’re declared (see the use of AuthenticationManagerBuilder).
 * In order to restricts access to specific http requests, the HttpSecurity is configured.
 * 
 * @author Giuseppe Urso
 * @see https://docs.spring.io/spring-security/site/docs/current/reference/html5/#getting-started
 * @see https://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebappSecurity extends WebSecurityConfigurerAdapter{

	private static final Logger logger = LoggerFactory.getLogger(WebappSecurity.class);
	private String user = "admin";
	// Spring 5 encodes passwords using BCrypt hashing by default.
	// Prefixing a plain text password with {noop}, no password encoder will be used.
	private String passwd = "{noop}12345";
	
	/**
	 * Overrides the HttpSecurity configuration requests mapping.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Restricting access to specific http requests...");
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/res/**").permitAll() 
				.anyRequest().fullyAuthenticated()
			//.and().httpBasic()
			.and().formLogin().permitAll().defaultSuccessUrl("/",true)
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	
	/**
	 * Configures multiple Authentication providers. 
	 * AuthenticationManagerBuilder allows for easily building multiple authentication mechanisms in the order they're declared.
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
			logger.info("Authentication Provider [IN-MEMORY]");
			auth.inMemoryAuthentication().withUser(user).password(passwd).roles("USER");				
	}
	
}
