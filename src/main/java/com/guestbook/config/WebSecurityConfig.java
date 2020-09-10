package com.guestbook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.guestbook.service.CustomUserDetailsService;

/**
 * Config class for Security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // permit access to resources like js and css
				.antMatchers("/", "/guestbook", "/registration", "/register", "/resources/**").permitAll() // permit anonymous access
				.anyRequest().authenticated() // secure everything else
				.and().csrf().disable() // disable csrf until further config added
				.formLogin().loginPage("/login").defaultSuccessUrl("/guestbook", true).permitAll() // configure login
				.and().logout().permitAll(); // configure logout
	}

}
