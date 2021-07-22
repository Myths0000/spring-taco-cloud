package tacos.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	/*
	 * 设置不需要控制器的简单页面
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registy){
		registy.addViewController("/").setViewName("home");
		registy.addViewController("/login");
	}
}
