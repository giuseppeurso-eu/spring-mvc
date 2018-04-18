package eu.giuseppeurso.spring.mvc.annotation.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * The Servlet Context configurator which will be detected and bootstrapped automatically by any Servlet 3.0+ container.
 * This class allows you to configure the ServletContext programmatically, as opposed to the traditional web.xml-based approach.
 * 
 * @author Giuseppe Urso
 *
 */
public class WebappInitializer implements WebApplicationInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(WebappInitializer.class);
	
	/**
	 * Configures the given ServletContext with any servlets, filters, listeners context-params and attributes
	 * necessary for initializing this web application
	 */
	@Override
	public void onStartup(ServletContext container) throws ServletException {
		logger.info(" >>>>> INITIALIZING WEBAPP ["+container.getServletContextName()+"]..."); 
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        
        logger.info("Registering class: "+WebappConfig.class.getSimpleName());
        ctx.register(WebappConfig.class);
        // Add here your classes ...
        
        ctx.setServletContext(container);
 
        logger.info("Adding servlet: "+DispatcherServlet.class.getSimpleName());
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
 
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }

	
}
