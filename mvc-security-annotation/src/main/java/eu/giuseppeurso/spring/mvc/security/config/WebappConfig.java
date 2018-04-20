package eu.giuseppeurso.spring.mvc.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * The Webapp configuration class. It contains all the information required for
 * component-scanning and view resolver.
 * 
 * @author Giuseppe Urso - www.giuseppeurso.eu
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "eu.giuseppeurso.spring.mvc.security")
public class WebappConfig implements WebMvcConfigurer {

	/**
	 * This allows for mapping the Spring MVC DispatcherServlet to "/", while still allowing static resource requests mapping to be handled by the container’s default Servlet. 
	 * A customized InternalResourceViewResolver bean can be used to view resources without the need for a dedicated mapping to be defined for each view (see viewResolver()).
	 *  
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	/**
	 * Allows for direct resolution of symbolic view names to URLs, without explicit
	 * mapping definition.
	 * 
	 * @return
	 */
	@Bean
	public InternalResourceViewResolver htmlViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		// It allows you to resolve multiple types of page (.html, .jsp, ...) in the same view directory.
		viewResolver.setSuffix("");
		return viewResolver;
	}
	
	/**
	 * Stores registrations of resource handlers for serving static resources such as images, css files and so on.
	 */
	@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		// The URL pattern corresponding to static resources located in "/WEB-INF/view/resources/"
        registry.addResourceHandler("/res/**").addResourceLocations("/WEB-INF/view/resources/");        
    }

}
