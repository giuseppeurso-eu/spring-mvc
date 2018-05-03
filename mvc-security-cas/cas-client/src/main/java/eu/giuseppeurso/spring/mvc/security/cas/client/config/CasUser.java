package eu.giuseppeurso.spring.mvc.security.cas.client.config;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class CasUser implements UserDetails{

	public static final Logger logger = LoggerFactory.getLogger(CasUser.class);
	private static final long serialVersionUID = 1L;
	
	private User user;
	private Map<String, Object> attributes;
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	@Override
	public String getUsername() {
		return user.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		return user.isAccountNonExpired();
	}
	@Override
	public boolean isAccountNonLocked() {
		return user.isAccountNonLocked();
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return user.isCredentialsNonExpired();
	}
	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	/**
	 * Getter and setter for the default Spring Security User object.
	 * @return
	 */
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Getter and setter for additional user details.
	 * 
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
