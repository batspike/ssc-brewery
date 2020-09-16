package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest
class BeerControllerIT extends BaseIT {

	@Test
	@WithMockUser("spring") // any user id will cause the mock to pass
	void findBeers() throws Exception {
		mockMvc.perform(get("/beers/find"))
				.andExpect(status().isOk())
				.andExpect(view().name("beers/findBeers"))
				.andExpect(model().attributeExists("beer"))
				;
	}

	//alternately we test the authentication using httpBasic()
	//in this case a valid userid/password is verified or else result in 401 - unauthorized
	@Test
	void findBeersWithHttpBasic() throws Exception {
		mockMvc.perform(get("/beers/find").with(httpBasic("spring", "guru")))
		.andExpect(status().isOk())
		.andExpect(view().name("beers/findBeers"))
		.andExpect(model().attributeExists("beer"))
		;
	}
	
	
	@Test
	void findBeersNoAuthenticationTest() throws Exception { //see security config
		mockMvc.perform(get("/beers/find"))
		.andExpect(status().isOk());
	}
}
