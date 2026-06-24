package com.azercell.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class MarketplaceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarketplaceApplication.class, args);
	}
}
