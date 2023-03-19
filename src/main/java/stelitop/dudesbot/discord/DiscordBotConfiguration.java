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

import java.io.File;
import java.io.IOException;

@Configuration
public class DiscordBotConfiguration {

    @Autowired
    private Environment environment;

    /**
     * Bean for the discord gateway client
     *
     * @return
     * @throws IOException
     */
    @Bean
    public GatewayDiscordClient gatewayDiscordClient() throws IOException {
        ObjectMapper om = new ObjectMapper();
        boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));
        DiscordBotToken token;
        if (devmode) {
            token = om.readValue(new File("testbotconfig.json"), DiscordBotToken.class);
        } else {
            token = om.readValue(new File("botconfig.json"), DiscordBotToken.class);
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
