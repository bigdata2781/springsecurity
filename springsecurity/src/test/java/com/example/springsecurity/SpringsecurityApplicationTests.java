package com.example.springsecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SpringsecurityApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	
	@Test
	void contextLoadsHeaderForSSO() throws Exception {
		this.mockMvc.perform(get("http://localhost:8080/blogs").header("SM_USER", "naveen")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void contextLoadsParamForLocal() throws Exception {
		System.setProperty("", "");
		this.mockMvc.perform(get("http://localhost:8080/blogs?SM_USER=naveen")).andDo(print()).andExpect(status().isOk());
	}

}