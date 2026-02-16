package com.autummata.treeko;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class ExpenseIntegrationTest {
	@Autowired
	WebApplicationContext webApplicationContext;

	MockMvc mockMvc;

	final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}

	@Test
	void expensesRequireAuthentication() throws Exception {
		mockMvc.perform(get("/api/expenses"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void canCreateListGetUpdateDeleteExpense() throws Exception {
		String username = "user_" + UUID.randomUUID().toString().replace("-", "");
		String token = registerAndGetToken(username);

		// create
		MvcResult created = mockMvc
				.perform(post("/api/expenses")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Lunch\",\"cost\":14.50,\"type\":\"PERSONAL\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("Lunch"))
				.andExpect(jsonPath("$.cost").value(14.50))
				.andExpect(jsonPath("$.type").value("PERSONAL"))
				.andReturn();

		JsonNode createdJson = objectMapper.readTree(created.getResponse().getContentAsString());
		long id = createdJson.get("id").asLong();
		assertThat(id).isPositive();

		// list includes it
		mockMvc.perform(get("/api/expenses").header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value((int) id));

		// get by id
		mockMvc.perform(get("/api/expenses/" + id).header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value((int) id));

		// update
		mockMvc.perform(put("/api/expenses/" + id)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Lunch (updated)\",\"cost\":15.00,\"type\":\"WORK\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Lunch (updated)"))
				.andExpect(jsonPath("$.cost").value(15.00))
				.andExpect(jsonPath("$.type").value("WORK"));

		// delete
		mockMvc.perform(delete("/api/expenses/" + id).header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());

		// now missing
		mockMvc.perform(get("/api/expenses/" + id).header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	private String registerAndGetToken(String username) throws Exception {
		String email = username + "@example.com";
		MvcResult result = mockMvc
				.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"email\":\"" + email
								+ "\",\"password\":\"password1234\"}"))
				.andExpect(status().isOk())
				.andReturn();
		JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
		return json.get("token").asText();
	}
}
