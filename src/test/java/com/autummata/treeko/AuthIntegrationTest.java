package com.autummata.treeko;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class AuthIntegrationTest {
	@Autowired
	WebApplicationContext webApplicationContext;

	final ObjectMapper objectMapper = new ObjectMapper();

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}

	@Test
	void registerReturnsBearerToken() throws Exception {
		String username = "user_" + UUID.randomUUID().toString().replace("-", "");
		String email = username + "@example.com";

		MvcResult result = mockMvc
				.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"email\":\"" + email
								+ "\",\"password\":\"password1234\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tokenType").value("Bearer"))
				.andExpect(jsonPath("$.token").isString())
				.andReturn();

		JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
		assertThat(json.get("token").asText()).isNotBlank();
	}

	@Test
	void loginWorksAfterRegister() throws Exception {
		String username = "user_" + UUID.randomUUID().toString().replace("-", "");
		String email = username + "@example.com";

		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"" + username + "\",\"email\":\"" + email
						+ "\",\"password\":\"password1234\"}"))
				.andExpect(status().isOk());

		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"" + username + "\",\"password\":\"password1234\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tokenType").value("Bearer"))
				.andExpect(jsonPath("$.token").isString());
	}

	@Test
	void loginWithBadCredentialsReturns400() throws Exception {
		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"nope\",\"password\":\"wrong\"}"))
				.andExpect(status().isBadRequest());
	}
}
