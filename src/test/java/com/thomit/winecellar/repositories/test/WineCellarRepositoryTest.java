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
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.thomit.winecellar.WineCellarApp;
import com.thomit.winecellar.models.Wine;
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
@ActiveProfiles("dev")
public class WineCellarRepositoryTest {

	@Autowired
	private WineRepository wineRepository;

	@Autowired
	private WebApplicationContext webAppContext;

	private MockMvc mockMvc;

	private List<Wine> wineList = new ArrayList<>();
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	private MediaType contentType = new MediaType("application", "hal+json", Charset.forName("UTF-8"));
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

	@Before
	public void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(webAppContext).build();
		this.wineRepository.deleteAllInBatch();

		Wine testWine = createTestWine();
		this.wineList.add(this.wineRepository.save(testWine));

	}

	@Test
	public void testWineEndpointGetStatusOk() throws Exception {
		this.mockMvc.perform(get("/api/wine"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testWineFound() throws Exception {
		this.mockMvc.perform(get("/api/wine/" + this.wineList.get(0).getId()).contentType(contentType).accept(contentType))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.name", is(this.wineList.get(0).getName())))
		.andExpect(jsonPath("$.producer", is(this.wineList.get(0).getProducer())))
		.andExpect(jsonPath("$.price", is(this.wineList.get(0).getPrice().doubleValue())))
		.andExpect(jsonPath("$.merchant", is(this.wineList.get(0).getMerchant())));
	}
	
	@Test
	public void createWine() throws Exception {
		String wineJson = json(new Wine("Ygay", 24, "Globus"));
        this.mockMvc.perform(post("/api/wine")
                .contentType(contentType)
                .content(wineJson))
                .andExpect(status().isCreated());
	}

	private Wine createTestWine() {
		Wine wine = new Wine();
		wine.setName("TestWine");
		wine.setYear(2012);
		wine.setCountry("Chile");
		wine.setPrice(34.5D);
		wine.setQuantity(5);
		wine.setRegion("Bio Bio");
		wine.setProducer("Viña Errazuriz");
		wine.setMerchant("Mövenpick");
		return wine;
	}
	
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}