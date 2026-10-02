package de.douda2209.adopt_a_pet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AdoptAPetApplicationTests {

	@Test
	void contextLoads() {
	}

}
