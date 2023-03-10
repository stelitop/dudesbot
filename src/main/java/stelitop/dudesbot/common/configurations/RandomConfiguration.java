package stelitop.dudesbot.common.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomConfiguration {

    /**
     * Gets the random generator shared between components.
     *
     * @return The random generator.
     */
    @Bean
    public Random random() {
        return new Random();
    }
}
