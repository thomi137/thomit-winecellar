package com.thomit.winecellar.repositories.test;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomit.winecellar.WineCellarApp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WineCellarApp.class)
@WebIntegrationTest(randomPort = true)
@OAuth2ContextConfiguration(ClientDetails.class)
@ActiveProfiles("test")
public class WineCellarSecurityTest {

	@Autowired
	WebApplicationContext context;

	@Autowired
	FilterChainProxy filterChain;

	private MockMvc mvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setup() {
		this.mvc = webAppContextSetup(this.context)
				.addFilters(this.filterChain).build();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void everythingSecure() throws Exception {
		this.mvc.perform(get("/wine").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
		this.mvc.perform(
				get("/wine/1/wines").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	public void testClientCredentialsGrant() throws Exception {
		String header = "Basic "
				+ new String(
						Base64.encode("test-client:test-secret".getBytes()));
		MvcResult result = this.mvc
				.perform(
						post("/oauth/token").header("Authorization", header)
						.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
								.param("grant_type", "password")
								.param("username", "test-user")
								.param("password", "test-user-secret"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		/**
		 * Object accessToken = this.objectMapper
		 * .readValue(result.getResponse().getContentAsString(), Map.class)
		 * .get("access_token");
		 * 
		 * MvcResult wineAction =
		 * this.mvc.perform(get("/wine").accept(MediaTypes.HAL_JSON)
		 * .header("Authorization", "Bearer " + accessToken))
		 * .andExpect(header().string("Content-Type",
		 * MediaTypes.HAL_JSON.toString()))
		 * .andExpect(status().isOk()).andDo(print()).andReturn();
		 **/
	}

}

class ClientDetails extends ClientCredentialsResourceDetails {

}
