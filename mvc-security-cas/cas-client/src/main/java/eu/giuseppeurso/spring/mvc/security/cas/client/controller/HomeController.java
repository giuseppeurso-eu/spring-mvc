package eu.giuseppeurso.spring.mvc.security.cas.client.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
	
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView defaultView (HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("Session-ID: "+request.getSession().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			logger.info("Logged user: ["+userDetails.getUsername()+"]");
		}else {
			logger.warn("Unable to retrieve logged user from SecurityContextHolder.");
		}
		
        String pageName = "index.html";
        logger.info("Showing page: '"+pageName+"'");
        ModelAndView view = new ModelAndView(pageName);
        return view;
    }

	@RequestMapping(value = {"/contacts"}, method = RequestMethod.GET)
    public ModelAndView contactsView (HttpServletRequest request, HttpServletResponse response) {        String pageName = "contacts.html";
    	logger.info("Session-ID: "+request.getSession().getId());    
    	logger.info("Showing page: '"+pageName+"'");
        ModelAndView view = new ModelAndView(pageName);
        return view;
    }
	
	@GetMapping("/login-cas")
	public String login(HttpServletRequest request) {
		logger.info("Session-ID: "+request.getSession().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			logger.info("Logged user: ["+userDetails.getUsername()+"]");
		}else {
			logger.warn("Unable to retrieve logged user from SecurityContextHolder.");
		}
		logger.info("Redirecting to the root webcontext: /");
	    return "redirect:/";
	}
}
