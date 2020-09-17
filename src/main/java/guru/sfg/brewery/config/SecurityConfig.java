package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
		RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
		filter.setAuthenticationManager(authenticationManager);
		
		return filter;
	}

	@Override
	// this setup the Authorization (access to assets); 
	// override configure(AuthenticationManagerBuilder) to setup Authentication (logins)
	protected void configure(HttpSecurity http) throws Exception {
		//setup header filter to do authentication
		http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), 
													UsernamePasswordAuthenticationFilter.class)
			.csrf().disable();
		
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

	@Override
	// create in memory userid/password for authentication
	// three users using three difference password encoding based on prefix indicator
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("spring")
			.password("{sha256}f89e9d53b3f115efec93433621736df1da63b4fd18a22f5d012d6b80074408c0ed389c403808d7e3")
			.roles("ADMIN")
			.and()
			.withUser("user")
			.password("{bcrypt}$2a$16$rQk2X/DCeN0lbU55ODew8eDkVGdegjJY7CRmYjy5LkLOxGj7xUNIm")
			.roles("USER")
			.and()
			.withUser("scott")
			.password("{ldap}{SSHA}HyCjZ2FM2KsAp6sYS4f9MQwyJFbjQBiBJIPksg==")
			.roles("CUSTOMER")
			;
	}
	@Bean
	// PasswordEncoder is required by AuthenticationManager
	protected PasswordEncoder getPasswordEncoder() {
		// use our custom factories to generate a list of password encoders for delegation
		return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder(); 
	}
	
	
	
	
}
