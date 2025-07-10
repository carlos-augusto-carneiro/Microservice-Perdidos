package com.recupera.item.back.usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.recupera.item.back.usuarios.config.TestConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class RecuperaApplicationTests {

	@Test
	void contextLoads() {
	}

}
