package org.javacoders.blog.config;

import org.javacoders.blog.exceptions.ResourceNotFoundException;
import org.javacoders.blog.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class ApplicationConfig {
	
	private final UserRepository repository;
	
	@Bean
	public UserDetailsService userdetailsService() {
		return username -> this.repository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email "+username, 0));
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userdetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
//	@Autowired
//    void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(userdetailsService())
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
	
	@Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
         AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
         authenticationManagerBuilder
               .userDetailsService(userdetailsService())
               .passwordEncoder(passwordEncoder());
         return authenticationManagerBuilder.build();
     }
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
