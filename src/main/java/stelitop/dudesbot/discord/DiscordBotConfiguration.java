package stelitop.dudesbot.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class DiscordBotConfiguration {

    @Autowired
    private Environment environment;
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Bean for the discord gateway client
     *
     * @return
     * @throws IOException
     */
    @Bean
    public GatewayDiscordClient gatewayDiscordClient() throws IOException {

        ObjectMapper om = new ObjectMapper();

        PathMatchingResourcePatternResolver matcher = new PathMatchingResourcePatternResolver();
        boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));
        DiscordBotToken token;
        if (devmode) {
            token = om.readValue(resourceLoader.getResource("classpath:testbotconfig.json").getInputStream(), DiscordBotToken.class);
            //token = om.readValue(ResourceUtils.getFile("classpath*:testbotconfig.json"), DiscordBotToken.class);
            //token = om.readValue(new File("testbotconfig.json"), DiscordBotToken.class);
            //token = om.readValue(matcher.getResources("classpath:testbotconfig.json")[0].getFile(), DiscordBotToken.class);
        } else {
            token = om.readValue(resourceLoader.getResource("classpath:botconfig.json").getInputStream(), DiscordBotToken.class);
            //token = om.readValue(matcher.getResources("classpath:botconfig.json")[0].getFile(), DiscordBotToken.class);
        }
        return DiscordClientBuilder.create(token.getToken()).build()
                .gateway()
                .setInitialPresence(ignore -> ClientPresence.online(ClientActivity.playing("Battle Dudes")))
                .login()
                .block();
    }

    @Bean
    public RestClient discordRestClient(GatewayDiscordClient gatewayDiscordClient) {
        return gatewayDiscordClient.getRestClient();
    }
}
