package org.grade.starters.webutils.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerUIRedirectController implements WebMvcConfigurer{

	@Autowired
	private TopadWebProperties properties;
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(properties.getSwagger().getRedirectUrl(), "/swagger-ui.html");
    }
	
}
