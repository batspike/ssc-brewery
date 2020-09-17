package guru.sfg.brewery.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

public class RestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {

	public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Request is to process authentication");
		}
		
		try {
			Authentication authResult = attemptAuthentication(request, response);
			
			if(authResult != null) {
				successfulAuthentication(request, response, chain, authResult);
			}
			else {
				chain.doFilter(request, response);
			}
		}
		catch (AuthenticationException e) {
			unsuccessfulAuthentication(request,response,e);
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String userName = getUsername(request);
		String password = getPassword(request);
		
		if(userName == null) {
			userName = "";
		}
		if(password == null) {
			password = "";
		}
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName,password);
		
		if(!StringUtils.isEmpty(userName)) {
			return this.getAuthenticationManager().authenticate(token);
		}
		else {
			return null;
		}
	}

	private String getPassword(HttpServletRequest request) {
		return request.getHeader("Api-Secret");
	}

	private String getUsername(HttpServletRequest request) {
		return request.getHeader("Api-Key");
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {

		if (logger.isDebugEnabled()) {
			logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
					+ authResult);
		}

		SecurityContextHolder.getContext().setAuthentication(authResult);

	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
								AuthenticationException failed ) throws IOException, ServletException {
		SecurityContextHolder.clearContext();

		if (logger.isDebugEnabled()) {
			logger.debug("Authentication request failed: " + failed.toString(), failed);
			logger.debug("Updated SecurityContextHolder to contain null Authentication");
		}

		response.sendError(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase());
	}

}
