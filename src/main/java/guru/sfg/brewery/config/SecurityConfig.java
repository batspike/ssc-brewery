package guru.sfg.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorize -> { //no authorization needed to access the following assets
				authorize.antMatchers("/","/webjars/**","/login","/resources/**").permitAll();
				authorize.antMatchers("/beers/find","/beers").permitAll();
				authorize.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
			})
			.authorizeRequests() //any other resource access will need authorization
				.anyRequest().authenticated()
			.and()
			.formLogin()
			.and()
			.httpBasic(); //use httpBasic authorization (Basic Auth), not secure unless use https
	}

}
