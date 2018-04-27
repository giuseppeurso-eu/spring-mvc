package eu.giuseppeurso.spring.mvc.security.cas.client.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom event Listener. Common way to change the default interval of the http session before the servlet container will 
 * invalidate it (for Tomcat default is 30 minutes).
 *  
 * @author www.giuseppeurso.eu
 *
 */
public class SessionListener implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);
	private int intervalSeconds = 30;
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		logger.info("SESSION-ID created: "+event.getSession().getId()+" (inactive interval "+intervalSeconds+" seconds)");
		event.getSession().setMaxInactiveInterval(intervalSeconds);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		logger.info("SESSION-ID ["+event.getSession().getId()+"] is destroyed after "+intervalSeconds+" (seconds).");
	}
	
}
