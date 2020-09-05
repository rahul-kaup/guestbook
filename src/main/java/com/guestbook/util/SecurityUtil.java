package com.guestbook.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
	/**
	 * Returns the username of the currently logged in user
	 * 
	 * @return username
	 */
	public static String getLoggedInUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/**
	 * Specifies whether the currently logged in user is of 'admin' role
	 * 
	 * @return
	 */
	public static boolean isLoggedInUserOfRoleAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

}