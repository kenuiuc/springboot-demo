package com.ken.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;

import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {


	@Autowired
	private HttpMessageConverter<MultiValueMap<String, ?>> converter;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testConverterType(){
		Assertions.assertNotNull(converter);
		String className = converter.getClass().getName();
		Assertions.assertEquals("com.ken.demo.CustomizedFormHttpMsgConverter", className);
	}

	@Test
	public void putIllegalUserTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.put("/user")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("name=ken%"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void getUserTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.
				get("/user"))
				.andExpect(status().isOk())
				.andExpect(content().string("KEN"));
	}

	@Test
	@Disabled
	public void contextLoads() {
	}

	@Test
    @Disabled
	public void catchAgain() {
		try {
			throw new SQLException();
		} catch (SQLException sqlEx) {
			log.error("Caught SQLException", sqlEx);
		} catch (Throwable th) {
			log.error("Caught throwable", th);
		}
	}


	@Test
    @Disabled
	public void testReThrow() throws SQLException {
		try {
			throw new SQLException("conflict");
		} catch (SQLException ex) {
			if (ex.getMessage().equals("conflict")) {
				log.error("Caught conflict SQLException", ex);
			} else {
				throw ex;
			}
		} catch (Throwable th) {
			// this wont catch the re-throw-ed exception from the same level catch block !!!
			log.error("Caught Throwable", th);
		}
	}

}
