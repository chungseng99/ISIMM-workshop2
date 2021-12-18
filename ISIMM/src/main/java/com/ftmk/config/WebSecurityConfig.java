package com.ftmk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ftmk.Authentication.AuthenticationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	AuthenticationService authenthicationService;
	
	
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    //retrieve encoded password
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.userDetailsService(authenthicationService);//give permission to user
		auth.authenticationProvider(authenticationProvider());//get encoded password
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		//no role required to access login page
		http.authorizeRequests().antMatchers("/").permitAll();
		//Privilege for admin to access admin dashboard
		http.authorizeRequests().antMatchers("/adminDashboard").access("hasRole('ROLE_ADMIN')");
//		//Privilege for user to access user dashboard
//		http.authorizeRequests().antMatchers("").access("hasRole('ROLE_STUDENT')");
//		//Privilege for parent to access parent dashboard
//		http.authorizeRequests().antMatchers("").access("hasRole('ROLE_PARENT')");
		//Privilege for admin clerk to access clerk dashboard
		http.authorizeRequests().antMatchers("/clerkDashboard").access("hasRole('ROLE_CLERK')");
//		//Privilege for teacher to access teacher dashboard
//		http.authorizeRequests().antMatchers("/teacherDashboard").access("hasRole('ROLE_TEACHER')");
		
		http.authorizeRequests().and().formLogin()
		//Submit URL of login page
		.loginProcessingUrl("/j_spring_security_check")
		.loginPage("/")
		.defaultSuccessUrl("/default")
		.failureUrl("/?error=true")
		.usernameParameter("username")
		.passwordParameter("password")
		.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");
		
	}
	
	

}
