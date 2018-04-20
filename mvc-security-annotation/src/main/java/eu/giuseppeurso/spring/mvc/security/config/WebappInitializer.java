package eu.giuseppeurso.spring.mvc.security.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
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
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info(" >>>>> INITIALIZING WEBAPP ["+servletContext.getServletContextName()+"]..."); 
        AnnotationConfigWebApplicationContext webappContext = new AnnotationConfigWebApplicationContext();
        
        // Add here your classes to the application context ...
        logger.info("Registering class: "+WebappConfig.class.getSimpleName());
        webappContext.register(WebappConfig.class);
        webappContext.setServletContext(servletContext);
 
//        logger.info("Adding a Loader Listener to the servlet context (manages the lifecycle of the root application context)...");
//		servletContext.addListener(new ContextLoaderListener(context));
		
        // In a Servlet 3.0+ environment, we can configure the Servlet container programmatically as an alternative of web.xml 
        logger.info("Registering class: "+DispatcherServlet.class.getSimpleName());
        ServletRegistration.Dynamic servlet = servletContext.addServlet("myDispatcher", new DispatcherServlet(webappContext));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
        
        // Registering the bean named 'springSecurityFilterChain' as DelegatingFilterProxy which allows you to enable Spring Security 
        logger.info("Registering class: "+DelegatingFilterProxy.class.getSimpleName());
		FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("mySpringSecurityFilter", new DelegatingFilterProxy("springSecurityFilterChain"));
		springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");
    }

	
}
