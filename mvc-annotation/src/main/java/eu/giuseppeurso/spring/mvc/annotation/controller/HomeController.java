package eu.giuseppeurso.spring.mvc.annotation.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
	
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView defaultView (HttpServletRequest request, HttpServletResponse response) {
        String pageName = "index.html";
        logger.info("Showing page: '"+pageName+"'");
        ModelAndView view = new ModelAndView(pageName);
        return view;
    }
	
    @RequestMapping(value = {"/contacts"}, method = RequestMethod.GET)
    public ModelAndView contactsView(HttpServletRequest request, HttpServletResponse response) {
        String pageName = "contacts.jsp";
        logger.info("Showing page: '"+pageName+"'");
        ModelAndView view = new ModelAndView("contacts.jsp");
        view.addObject("name", "Lupin the 3rd");
        view.addObject("email", "lupin@monkey.punch.org");
        view.addObject("tel", "1231234433");
        return view;
    }
}
