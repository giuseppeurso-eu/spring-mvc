package eu.giuseppeurso.spring.interceptor;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import eu.giuseppeurso.spring.interceptor.LoginForm;

/**
 * Handles requests for the application pages.
 * 
 * @author Giuseppe Urso
 */
@Controller
public class SampleController {
	
	private static final Logger logger = LoggerFactory.getLogger(SampleController.class);
	static String bundle = "configuration";
	public static ResourceBundle settings = ResourceBundle.getBundle(bundle);
	
	/**
	 * The request mapper for welcome page
	 * @return
	 */
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	  public String welcome() {
	    return "welcome";
	  }
	
	/**
	 * Simply selects the login view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showLogin(Model model, LoginForm loginform) {
		logger.info("Login page");
		if (!model.containsAttribute("error")) {
			model.addAttribute("error", false);
		}
		model.addAttribute("loginAttribute", loginform);
		return "login";
	}
	
	/**
	 * The POST method to submit login credentials.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(Model model, LoginForm loginform, Locale locale, HttpServletRequest request) throws Exception {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,locale);
		String formattedDate = dateFormat.format(date);
		
		String username = loginform.getUsername();
		String password = loginform.getPassword();
	
		
		logger.info("Login attempt for username "+ username+" at: "+formattedDate);
		
		// A simple authentication manager
		if(username != null && password != null){
			
			if( username.equals(settings.getString("username")) &&	password.equals(settings.getObject("password")) ){
				// Set a session attribute to check authentication then redirect to the welcome uri; 
				request.getSession().setAttribute("LOGGEDIN_USER", loginform);
				return "redirect:/welcome";
			}else{
				return "redirect:/login.failed";
			}
		}else{
			return "redirect:/login.failed";
		}
	}
		
	/**
	 * The login failed controller
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login.failed", method = RequestMethod.GET)
	public String loginFailed(Model model, LoginForm loginForm) {
		logger.debug("Showing the login failed page");
		model.addAttribute("error", true);
		model.addAttribute("loginAttribute", loginForm);
		return "login";
	}
	
}
