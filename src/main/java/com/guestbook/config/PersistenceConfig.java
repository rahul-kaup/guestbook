package com.guestbook.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Config class for JPA
 */
@EnableJpaRepositories(basePackages = "com.guestbook.repository")
public class PersistenceConfig {

}
