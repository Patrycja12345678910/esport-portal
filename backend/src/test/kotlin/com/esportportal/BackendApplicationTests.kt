package com.esportportal

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = ["PANDASCORE_API_TOKEN=test-token"])
class BackendApplicationTests {

	@Test
	fun contextLoads() {
	}

}
