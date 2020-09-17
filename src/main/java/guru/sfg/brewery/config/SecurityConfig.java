package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	// this setup the Authorization (access to assets); 
	// override configure(AuthenticationManagerBuilder) to setup Authentication (logins)
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/","/webjars/**","/login","/resources/**").permitAll()
				.antMatchers("/beers/find","/beers").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
				.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll() //note we use mvcMatcher; works the same
				.anyRequest().authenticated()
		.and()
			.formLogin()
		.and()
			.httpBasic(); //use httpBasic authorization (Basic Auth), not secure unless use https
	}
/*  alternate way to code the above, seems more convoluted	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorize -> { //no authorization needed to access the following assets
				authorize
				.antMatchers("/","/webjars/**","/login","/resources/**").permitAll()
				.antMatchers("/beers/find","/beers").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
				.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll(); //note we use mvcMatcher; works the same
			})
			.authorizeRequests() //any other resource access will need authorization
			.anyRequest().authenticated()
		.and()
			.formLogin()
		.and()
			.httpBasic(); //use httpBasic authorization (Basic Auth), not secure unless use https
	}
*/

	@Override
	// create in memory userid/password for authentication
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("spring")
			.password("guru")
			.roles("ADMIN")
			.and()
			.withUser("user")
			.password("password")
			.roles("USER")
			.and()
			.withUser("scott")
			.password("tiger")
			.roles("CUSTOMER")
			;
	}
	@Bean
	// PasswordEncoder is required by AuthenticationManager
	protected PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
/*	//alternate way in creating users for authentication
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails admin = User.withDefaultPasswordEncoder()
				.username("spring")
				.password("guru")
				.roles("ADMIN")
				.build();
		
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(admin,user);
	}	
*/
	
	
	
	
}
