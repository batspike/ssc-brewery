package guru.sfg.brewery.web.controllers.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import guru.sfg.brewery.web.controllers.BaseIT;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/api/v1/beer"))
				.andExpect(status().isOk());
	}
	
	@Test
	void findBeerById() throws Exception {
		mockMvc.perform(get("/api/v1/beer/97df0c39-90c5-4ae0-b663-453e8e19c312"))
			.andExpect(status().isOk());
	}

	@Test
	void findBeerByUpc() throws Exception {
		mockMvc.perform(get("/api/v1/beerUpc/0631234300019"))
			.andExpect(status().isOk());
	}
}
