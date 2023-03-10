package stelitop.dudesbot.discord.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class ButtonListener implements ApplicationRunner {

    @Autowired
    private GatewayDiscordClient gatewayDiscordClient;
    @Autowired
    private List<IActionComponentListener<ButtonInteractionEvent>> listeners;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        gatewayDiscordClient.on(ButtonInteractionEvent.class, event -> {
            var listener = listeners.stream()
                    .filter(Objects::nonNull)
                    .filter(x -> x.getComponentId().equals(event.getCustomId())).findFirst();
            if (listener.isEmpty()) {
                return Mono.empty();
            }
            return listener.get().handle(event);
        }).subscribe();
    }
}
