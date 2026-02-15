package com.ticketing.ticketsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackages = { "com", "com.ticketing.ticketsystem" })
class TicketsystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
