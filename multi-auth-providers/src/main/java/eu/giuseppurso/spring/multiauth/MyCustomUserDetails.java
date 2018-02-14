package eu.giuseppurso.spring.multiauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MyCustomUserDetails implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private List<SimpleGrantedAuthority> authorities=null;

	// Custom User details
	private String address;
	private String email;
	
	
	/**
	 * Getter and setter for the Spring Security default User object.
	 * @return
	 */
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Getters and setters for the custom UserDetails
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	/**
	 * Getters and setters for the inherited attributes of the UserDetails object.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<SimpleGrantedAuthority> authorities)
    {
		authorities=new ArrayList<SimpleGrantedAuthority>();
        this.authorities=authorities;
    }
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



}
