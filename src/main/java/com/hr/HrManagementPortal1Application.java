package com.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HrManagementPortal1Application {

	public static void main(String[] args) {
		SpringApplication.run(HrManagementPortal1Application.class, args);
	}

}
