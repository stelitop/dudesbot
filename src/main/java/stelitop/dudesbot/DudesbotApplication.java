package stelitop.dudesbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("stelitop.dudesbot.database.repositories")
public class DudesbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DudesbotApplication.class, args);
	}

}
