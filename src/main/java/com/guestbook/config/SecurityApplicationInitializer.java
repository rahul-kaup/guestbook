package com.guestbook.config;

import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * Initializer class secure web application
 */
public class SecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {

		// add support for file uploads
		insertFilters(servletContext, new MultipartFilter());
	}
}