package org.edupoll.app.config;

import org.edupoll.app.config.support.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, SimpMessagingTemplate simpMessagingTemplate) throws Exception {
		http.csrf(t -> t.disable());
		http.authorizeHttpRequests(t -> t.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
				.requestMatchers("/static/**").permitAll() //
				.requestMatchers("/", "/index", "/account/**", "/api/**").permitAll() //
				.anyRequest().authenticated());

		http.formLogin(t -> t.loginPage("/account/login").permitAll()
				.successHandler(new CustomAuthenticationSuccessHandler(simpMessagingTemplate)));

		http.logout(t -> t.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());

		http.anonymous(t -> t.disable());

		return http.build();
	}
}
