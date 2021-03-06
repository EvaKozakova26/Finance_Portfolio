package com.mystocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// TODO: 06.03.2021 configure security, this is temp solution to exclude security which is not necessary in this stage of development
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MystocksApplication {

	public static void main(String[] args) {
		SpringApplication.run(MystocksApplication.class, args);
	}

}
