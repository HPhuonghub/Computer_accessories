package com.dev.computer_accessories;

import com.dev.computer_accessories.configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ComputerAccessoriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComputerAccessoriesApplication.class, args);
	}

}
