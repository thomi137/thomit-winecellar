package com.thomit.winecellar.repositories.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.thomit.winecellar.WineCellarApp;
import com.thomit.winecellar.models.Account;
import com.thomit.winecellar.models.Wine;
import com.thomit.winecellar.repositories.AccountRepository;
import com.thomit.winecellar.repositories.WineRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.ContentResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WineCellarApp.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class WineCellarRepositoryTest {

	@Autowired
	private WineRepository wineRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private WebApplicationContext webAppContext;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private MockMvc mockMvc;

	private List<Wine> wineList = new ArrayList<>();
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	private MediaType contentType = new MediaType("application", "hal+json", Charset.forName("UTF-8"));
	
	private String userId = "user";
	
	private String rawPassword = "secret";
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
	
	
	

	@Before
	public void setUp() throws Exception {
		
		Account testAccount = new Account(userId, rawPassword, null, null, null, null, "ROLE_USER");
				
		this.mockMvc = webAppContextSetup(webAppContext).build();
		this.wineRepository.deleteAll();
		this.accountRepository.deleteAll();
		Account wineAccount = this.accountRepository.save(testAccount);
		Wine testWine = createTestWine(wineAccount);
		this.wineList.add(this.wineRepository.save(testWine));
		
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(userId, rawPassword, AuthorityUtils.createAuthorityList("ROLE_USER"));
		SecurityContextHolder.getContext().setAuthentication(principal);
	

	}

	@Test
	public void testWineEndpointGetStatusOk() throws Exception {
		
				
		this.mockMvc.perform(get("/wine/" + userId +"/wines"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testWineFound() throws Exception {
		this.mockMvc.perform(get("/wine/" + userId +"/wines/" + this.wineList.get(0).getId())
				.contentType(contentType).accept(contentType))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.wine.name", is(this.wineList.get(0).getName())))
		.andExpect(jsonPath("$.wine.producer", is(this.wineList.get(0).getProducer())))
		.andExpect(jsonPath("$.wine.price", is(this.wineList.get(0).getPrice().doubleValue())))
		.andExpect(jsonPath("$.wine.merchant", is(this.wineList.get(0).getMerchant())));
	}
	
	@Test(expected = NestedServletException.class)
	public void createWine() throws Exception {
		String wineJson = json(new Wine("Ygay", 24, "Globus"));
        this.mockMvc.perform(post("/wine/" + userId +"/wines")
                .contentType(contentType)
                .content(wineJson))
                .andExpect(status().is4xxClientError());
	}


	
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
	private Wine createTestWine(Account testAccount) {
		Wine wine = new Wine();
		wine.setName("TestWine");
		wine.setYear(2012);
		wine.setCountry("Chile");
		wine.setPrice(34.5D);
		wine.setQuantity(5);
		wine.setRegion("Bio Bio");
		wine.setProducer("Viña Errazuriz");
		wine.setMerchant("Mövenpick");
		wine.setAccount(testAccount);
		return wine;
	}
	
	private static class MockSecurityContext implements SecurityContext{

		private static final long serialVersionUID = 1L;
		private Authentication authentication;
		
		public MockSecurityContext(Authentication authentication){
			this.authentication = authentication;
		}
		
		@Override
		public Authentication getAuthentication() {
			return this.authentication;
		}

		@Override
		public void setAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}
		
	}

}