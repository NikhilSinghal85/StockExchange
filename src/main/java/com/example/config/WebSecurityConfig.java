package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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


	//    @Override
	//    protected void configure(final HttpSecurity http) throws Exception {
	//    	
	////        http
	////                .httpBasic().disable()
	////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	////                .authorizeRequests().antMatchers("/gs-guide-websocket","/", "/login**", "/stock").permitAll();
	//        
	//        http//.authorizeRequests().antMatchers("/gs-guide-websocket").permitAll().and()
	//        .authorizeRequests()
	//		.antMatchers("/", "/stock", "/BuySell", "/BuySell.html").access("hasRole('ROLE_USER')")
	//		.and()
	//			.formLogin().loginPage("/index.html").permitAll()
	//			.loginProcessingUrl("/login").permitAll()
	//			 .usernameParameter("user1")
	//             .passwordParameter("pass1")
	//			.successHandler(myAuthenticationSuccessHandler());
	//			//.defaultSuccessUrl("/BuySell");
	//    }


	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
		return new MySimpleUrlAuthenticationSuccessHandler();
	}

	
	
	
	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
}



