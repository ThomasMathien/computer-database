package com.excilys.computerDatabase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
 		http
 			.authorizeRequests()
 				.mvcMatchers("login").anonymous()
 				.mvcMatchers("addComputer", "editComputer").hasRole("ADMIN")
 				.mvcMatchers("dashboard").authenticated()
 				.and()
			.formLogin()
				.defaultSuccessUrl("/dashboard", false)
				.and()
			.logout();  

	}
	
 	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth.inMemoryAuthentication()
		.withUser("user").password(passwordEncoder().encode("azerty")).roles("USER")
		.and()
		.withUser("admin").password(passwordEncoder().encode("azerty")).roles("ADMIN");
 	}
 	
 	@Bean 
 	public PasswordEncoder passwordEncoder() { 
 	    return new BCryptPasswordEncoder(); 
 	}
	
}
