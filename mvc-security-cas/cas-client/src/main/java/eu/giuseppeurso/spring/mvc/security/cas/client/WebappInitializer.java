package eu.giuseppeurso.spring.mvc.security.cas.client;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import eu.giuseppeurso.spring.mvc.security.cas.client.config.CasConfigurer;
import eu.giuseppeurso.spring.mvc.security.cas.client.config.MvcConfigurer;
import eu.giuseppeurso.spring.mvc.security.cas.client.config.SecurityConfigurer;
import eu.giuseppeurso.spring.mvc.security.cas.client.listener.SessionListener;

/**
 * The Servlet Context initializer which will be detected and bootstrapped automatically by any Servlet 3.0+ container.
 * This class allows you to configure the ServletContext programmatically, as opposed to the traditional web.xml-based approach.
 * 
 * @author www.giuseppeurso.eu
 *
 */
public class WebappInitializer implements WebApplicationInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(WebappInitializer.class);
	
	/**
	 * Configures the given ServletContext with any servlets, filters, listeners, context-params and attributes
	 * necessary for initializing this web application.
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		// Creating the servlet application context by using 
		// annotation based context
		logger.info(" >>>>> INITIALIZING WEBAPP ["+servletContext.getServletContextName()+"]"); 
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        
        // Add here your configuration class for the 
        // application context (one or more annotated @Configuration classes to be processed)
        logger.info("Registering annotated (@Configuration) class: "+MvcConfigurer.class.getSimpleName());
        webApplicationContext.register(MvcConfigurer.class);
        logger.info("Registering annotated (@Configuration) class: "+CasConfigurer.class.getSimpleName());
        webApplicationContext.register(CasConfigurer.class);
        logger.info("Registering annotated (@Configuration) class: "+SecurityConfigurer.class.getSimpleName());
        webApplicationContext.register(SecurityConfigurer.class);
        webApplicationContext.setServletContext(servletContext);
        
        // Add here your http event Listeners to the servlet context 
        // (manages the lifecycle of the root application context)
        logger.info("Registering event Listener: "+ContextLoaderListener.class.getSimpleName());
		servletContext.addListener(new ContextLoaderListener(webApplicationContext));
		 logger.info("Registering event Listener: "+SessionListener.class.getSimpleName());
		servletContext.addListener(new SessionListener());
		
        // Add here your servlet classes (in a Servlet 3.0+ environment, we can configure 
		// the Servlet container programmatically as an alternative of web.xml)
		logger.info("Registering Servlet class: "+DispatcherServlet.class.getSimpleName());
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("giusWebappCentralDispatcher", new DispatcherServlet(webApplicationContext));
        servletRegistration.addMapping("/");
        
        // Add here the DelegatingFilterProxy which allows you to enable Spring Security and use
        // your custom Filters (according to the Spring Security internal infrastructure, target bean ID must be 'springSecurityFilterChain'). 
        logger.info("Registering Filters delegator class: "+DelegatingFilterProxy.class.getSimpleName());
		FilterRegistration.Dynamic ccc = servletContext.addFilter("giusWebappFilterDelegator", new DelegatingFilterProxy("springSecurityFilterChain"));
		ccc.addMappingForUrlPatterns(null, false, "/*");
    }

	
}
