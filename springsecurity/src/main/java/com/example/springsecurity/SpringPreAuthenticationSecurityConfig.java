package com.example.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@EnableWebSecurity
public class SpringPreAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AbstractPreAuthenticatedProcessingFilter filter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(filter, RequestHeaderAuthenticationFilter.class).authorizeRequests()
				.antMatchers("/static/**").permitAll().antMatchers("/").permitAll().antMatchers("/blogs").access("hasRole('ADMIN')").anyRequest().authenticated()
				;
	}
}
