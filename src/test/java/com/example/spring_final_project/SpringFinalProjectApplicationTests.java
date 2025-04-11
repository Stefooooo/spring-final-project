package com.example.spring_final_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ActiveProfiles("test")
@SpringBootTest
class SpringFinalProjectApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void testMainMethodRunsWithoutException() {
		assertDoesNotThrow(() -> Application.main(new String[]{}));
	}

}
