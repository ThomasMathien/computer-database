package com.excilys.computerDatabase.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"com.excilys.computerDatabase.controller.servlet",
		"com.excilys.computerDatabase.controller.cli",
		"com.excilys.computerDatabase.controller.page",
		"com.excilys.computerDatabase.controller.restController",
		"com.excilys.computerDatabase.mapper",
		"com.excilys.computerDatabase.service",
		"com.excilys.computerDatabase.validator",
		"com.excilys.computerDatabase.session",
		"com.excilys.computerDatabase.parser",
		"com.excilys.computerDatabase.config"})
@EnableWebMvc
public class SpringWebConfig implements WebMvcConfigurer {
 	
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");
		return bean;
	}
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       registry.addViewController("/login").setViewName("login");
    }
	   
	@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
	@Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("localization/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
	
    @Bean
    public LocaleResolver localeResolver() {
    	SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    	localeResolver.setDefaultLocale(new Locale("en"));
        return localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("locale");
        registry.addInterceptor(localeChangeInterceptor);
    }
    
}
