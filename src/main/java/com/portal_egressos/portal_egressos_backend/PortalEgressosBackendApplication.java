package com.portal_egressos.portal_egressos_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:${user.dir}/.env")
public class PortalEgressosBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalEgressosBackendApplication.class, args);
	}

}
