package guru.sfg.brewery.web.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTests {
	static final String PASSWORD = "password";
	
	@Test
	void hashingExample() {
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		
		String salted = PASSWORD + "ThisIsMySALtValue";
		System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
		System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
	}

	@Test
	void noOpEncoderTest() {
		PasswordEncoder noop = NoOpPasswordEncoder.getInstance();
		
		System.out.println(noop.encode(PASSWORD));
	}
	
	@Test
	void LDAP_EncoderTest() {
		PasswordEncoder ldap = new LdapShaPasswordEncoder();
		System.out.println(ldap.encode(PASSWORD));
		System.out.println(ldap.encode(PASSWORD));
		
		String encodedPassword = ldap.encode(PASSWORD);
		assertTrue(ldap.matches(PASSWORD, encodedPassword));
	}
}
