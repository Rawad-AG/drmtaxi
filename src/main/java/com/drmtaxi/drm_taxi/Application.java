package com.drmtaxi.drm_taxi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.drmtaxi.drm_taxi.Repositories.UserRepo;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.test.enabled", havingValue = "false", matchIfMissing = true)
	CommandLineRunner commandLineRunner(UserRepo repo, PasswordEncoder encoder) {
		return args -> {
		};
	}

}
