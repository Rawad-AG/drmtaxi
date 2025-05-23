package com.drmtaxi.drm_taxi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.drmtaxi.drm_taxi.Entities.UserEntity;
import com.drmtaxi.drm_taxi.Repositories.UserRepo;
import com.drmtaxi.drm_taxi.Utils.Gender;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepo repo, PasswordEncoder encoder) {
		return args -> {
			String password = encoder.encode("pass1234");

			List<UserEntity> users = new ArrayList<>(List.of(
					new UserEntity("John", "Doe", Gender.MALE, "john@example.com", null, password),
					new UserEntity("Jane", "Smith", Gender.FEMALE, "jane@example.com", null, password),
					new UserEntity("Alex", "Johnson", Gender.MALE, "alex@example.com", null, password),
					new UserEntity("Emily", "Brown", Gender.FEMALE, "emily@example.com", null, password),
					new UserEntity("Michael", "Wilson", Gender.MALE, "michael@example.com", null, password)));

			users.forEach(user -> user.getAuth().setEnable(true));

			repo.saveAll(users);
		};
	}

}
