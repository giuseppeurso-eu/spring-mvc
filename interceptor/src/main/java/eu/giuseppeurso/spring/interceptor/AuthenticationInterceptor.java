package eu.giuseppeurso.spring.interceptor;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import eu.giuseppeurso.spring.interceptor.LoginForm;

/**
 * The Authentication Interceptor class. This class implements the spring HandlerInteceptor
 * to processing requests into three steps:<br>
 * - pre-handle<br>
 * - post-handle<br>
 * - after-completition
 * 
 * @see org.springframework.web.servlet.HandlerInterceptor
 * @author Giuseppe Urso
 * 
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);
	static String bundle = "configuration";
	public static ResourceBundle settings = ResourceBundle.getBundle(bundle);

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {

		log.info("Interceptor: Pre-handle");

		// Avoid a redirect loop for some urls
		if( !request.getRequestURI().equals("/sample-interc/") &&
		    !request.getRequestURI().equals("/sample-interc/login.do") &&
		    !request.getRequestURI().equals("/sample-interc/login.failed"))
		  {
			  LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
		   if(userData == null)
		   {
		    response.sendRedirect("/sample-interc/");
		    return false;
		   }   
		  }
		  return true;
}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.debug("Post-handle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.debug("After-completion");
	}

}
