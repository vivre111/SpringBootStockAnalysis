package com.austin.demo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final LogoutHandler logoutHandler;

	public SecurityConfig(LogoutHandler logoutHandler) {
		this.logoutHandler = logoutHandler;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests().requestMatchers("/greeting","/","/home").permitAll().anyRequest().authenticated().and()
				.oauth2Login()
				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.addLogoutHandler(logoutHandler).and().csrf().disable()
				.build();
	}
}