package eu.giuseppeurso.spring.mvc.security.cas.client.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an example of custom event listener. The listener is only declared and not implemented.
 * To register the listener in the servlet context, you have to add that class in the WebappInitializer.
 * 
 * @author www.giuseppeuros.eu
 *
 */
public class ExampleListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(ExampleListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// your custom code here...
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// your custom code here...
		
	}
	

}
