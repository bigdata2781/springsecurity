package com.example.springsecurity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringPreAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	AbstractPreAuthenticatedProcessingFilter filter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(filter, RequestHeaderAuthenticationFilter.class).authorizeRequests()
				.antMatchers("/static/**").permitAll().antMatchers("/").permitAll().antMatchers("/blogs").access("hasRole('ADMIN')").anyRequest().authenticated()
				;
	}
	
	@Bean(name = "siteminderFilter")
	@Profile("uat")
	public AbstractPreAuthenticatedProcessingFilter siteminderFilter() throws Exception {
		System.out.println("----- UAt -----");
		RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
		requestHeaderAuthenticationFilter.setPrincipalRequestHeader("SM_USER");
		requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
		return requestHeaderAuthenticationFilter;
	}
	
	@Bean(name = "siteminderFilter")
	@Profile("local")
	public AbstractPreAuthenticatedProcessingFilter siteminderFilter1() throws Exception {
		System.out.println("----- LOCAL -----");
		RequestParamAuthenticationFilter requestParamAuthenticationFilter = new RequestParamAuthenticationFilter();
		requestParamAuthenticationFilter.setPrincipalRequestParam("SM_USER");
		requestParamAuthenticationFilter.setAuthenticationManager(authenticationManager());
		return requestParamAuthenticationFilter;
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		final List<AuthenticationProvider> providers = new ArrayList<>(1);
		providers.add(preauthAuthProvider());
		return new ProviderManager(providers);
	}
	@Bean(name = "preAuthProvider")
	PreAuthenticatedAuthenticationProvider preauthAuthProvider() throws Exception {
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
		return provider;
	}
	@Bean
	UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() throws Exception {
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
		wrapper.setUserDetailsService(customUserDetailsService);
		return wrapper;
	}
}
