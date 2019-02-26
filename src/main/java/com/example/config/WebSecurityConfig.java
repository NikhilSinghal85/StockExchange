package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;



@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	
	@Autowired
	  private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {

		authenticationMgr.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery("SELECT USERNAME, PASSWORD, Active" 
        		+ "  FROM hr.Login WHERE USERNAME=?")
        .authoritiesByUsernameQuery("select username, authority "
                + "from hr.authorities where username=?");
	}


// @nikhil uncomment it for junit test case this will remove authentication of user	
	  @Override
      public void configure(HttpSecurity http) throws Exception {
		  http.authorizeRequests().antMatchers("/**").permitAll();
		  // to enable post request
           http.cors().and().csrf().disable();
      }

	  
	  
	
	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
}



