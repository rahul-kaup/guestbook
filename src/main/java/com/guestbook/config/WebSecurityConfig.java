package com.guestbook.config;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

/**
 * Config class for Security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/guestbook").permitAll() // permit anonymous access to /guestbook
				.anyRequest().authenticated() // secure everything else
				.and().csrf().disable() 
				.formLogin().loginPage("/login").defaultSuccessUrl("/guestbook", true).permitAll() // configure login
				.and().logout().permitAll(); // configure logout
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {

		// read user details from db and populate in-memory user details manager
		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

		for (Iterator<User> iterator = userRepository.findAll().iterator(); iterator.hasNext();) {
			User user = iterator.next();
			inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build());
		}

		return inMemoryUserDetailsManager;
	}

}
