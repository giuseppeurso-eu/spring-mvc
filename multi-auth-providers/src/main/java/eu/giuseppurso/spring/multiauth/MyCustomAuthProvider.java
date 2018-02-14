package eu.giuseppurso.spring.multiauth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MyCustomAuthProvider implements AuthenticationProvider{
	
	/**
	 * Use credentials coming from the login page and authenticate against a third-party system.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		UsernamePasswordAuthenticationToken userAuth = null;
		
		System.out.println("Use credentials [" + username + "|*****] and authenticate against the third-party system...");
		
		// Put here your code for the remote authentication
		// Invoking the external authentication web service and make something with the response
		// ...
		String authenticationResponse = "";
		if (authenticationResponse.equals("user authenticated or something else")) {
				System.out.println("Connection to External Authentication Web Service succeeded for user: "+username);
				List<SimpleGrantedAuthority> authorities=new ArrayList<SimpleGrantedAuthority>();
				User user  = new User(username, password, true, true, true, true, authorities);
				MyCustomUserDetails userDetails = new MyCustomUserDetails();
				userDetails.setUser(user);
				userDetails.setAuthorities(authorities);
				System.out.println("Attaching some custom user details to the authenticated user...");
				userDetails.setAddress("The user address or something else returned by the external authentication service.");
				userDetails.setEmail("The user email or something else returned by the external authentication service.");
				//userAuth = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
				userAuth = new UsernamePasswordAuthenticationToken(userDetails, password, new ArrayList<>());
		}else {
				System.out.println("User ["+username+"] authentication failed! Wrong credentials or invalid account.");
		}
		return userAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}


}
