package eu.giuseppurso.spring.multiauth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {
	
	// In-Memory Provider Settings
	private Boolean inMemoryAuthEnabled=true;   
	private String inMemoryAuthUser="admin";
	private String inMemoryAuthPasswd="123";
	
	// JDBC Provider Settings
	private Boolean jdbcAuthEnabled=true;   
	private String jdbcAuthQuery="select username,password,enabled from USERS_TABLE where username=?";
	@Autowired
	private DataSource dataSource;
	
	// LDAP Provider Settings
	private Boolean ldapAuthEnabled=true;
	private String ldapAuthUrl="ldap://192.168.56.100:389";
	private String ldapAuthBaseDn="dc=ldap,dc=example,dc=com,dc=local";
	private String ldapAuthPrincipalUser="cn=Directory Manager";
	private String ldapAuthPrincipalPasswd="12345";
	private String ldapUsersSearchBase="ou=people";
	private String ldapUsersSearchFilter="uid={0}";
	private String ldapGroupsSearchBase="ou=groups";
	private String ldapGroupsSearchFilter="member={0}";
		
	// ACTIVE DIRECTROY Provider Settings
	private Boolean adAuthEnabled=true;
	private String adAuthUrl="ldap://192.168.56.200:389/";
	private String adAuthDomain="ad.example.com.local";
	private String adAuthBaseDn="dc=ad,dc=example,dc=com,dc=local";
	private String adAuthUsersFilter="(&(objectClass=user)(userPrincipalName={0}))";
	
	// REST/SOAP Web Services Provider Settings
	private Boolean wsAuthEnabled=false;   
	@Autowired
	private MyCustomAuthProvider wsAuthProvider;
	
	
	/**
	 * Configures multiple Authentication providers. 
	 * AuthenticationManagerBuilder allows for easily building multiple authentication mechanisms in the order they're declared.
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (inMemoryAuthEnabled) {
			System.out.println(">>>>> [IN-MEMORY] Authentication Provider enabled: "+inMemoryAuthEnabled);
			auth.inMemoryAuthentication().withUser(inMemoryAuthUser).password(inMemoryAuthPasswd).roles("USER");				
		}
		if (ldapAuthEnabled) {
			System.out.println(">>>>> [LDAP] Authentication Provider enabled: "+ldapAuthEnabled);
			DefaultSpringSecurityContextSource ldapContextSource = new DefaultSpringSecurityContextSource(ldapAuthUrl+"/"+ldapAuthBaseDn);
			if (ldapAuthPrincipalUser!=null) {
				ldapContextSource.setUserDn(ldapAuthPrincipalUser);
				ldapContextSource.setPassword(ldapAuthPrincipalPasswd);
				System.out.println("Initializing LDAP Source with Principal '"+ldapAuthPrincipalUser+"/****'");
			}
			ldapContextSource.afterPropertiesSet();
			auth.ldapAuthentication()
			.userSearchBase(ldapUsersSearchBase)
	        .userSearchFilter(ldapUsersSearchFilter)
	        .groupSearchBase(ldapGroupsSearchBase)
	        .groupSearchFilter(ldapGroupsSearchFilter)
	        .contextSource(ldapContextSource);	        
		}
		if (adAuthEnabled) {
			System.out.println(">>>>> [ACTIVE DIRECTORY] Authentication Provider enabled: "+adAuthEnabled);
			ActiveDirectoryLdapAuthenticationProvider adSource = new ActiveDirectoryLdapAuthenticationProvider(adAuthDomain, adAuthUrl, adAuthBaseDn);
			adSource.setConvertSubErrorCodesToExceptions(true);
			adSource.setSearchFilter(adAuthUsersFilter);
			auth.authenticationProvider(adSource);
			
		}
		if (jdbcAuthEnabled) {
			System.out.println(">>>>> [JDBC] Authentication Provider enabled: "+jdbcAuthEnabled);
			auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery(jdbcAuthQuery);
		}
		if (wsAuthEnabled) {
			System.out.println(">>>>> [REMOTE WEB SERVICE] Authentication Provider enabled: "+wsAuthEnabled);
			// Create your custom provider by implementing the AuthenticationProvider interface and set here the provider responsible for the external authentication.
			auth.authenticationProvider(wsAuthProvider);
		}
	}
	
	/**
	 * Overrides the HttpSecurity configuration requests mapping.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("Restricting access to specific http requests...");
		http
			.csrf().disable()
			.authorizeRequests().anyRequest().fullyAuthenticated()
			.and().httpBasic()
			.and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/home", true)
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}


}
