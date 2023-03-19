package stelitop.dudesbot.discord.commands.slashcommands.annotations;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

public class TestCommand {

    @SlashCommand(
            name = "istoggled",
            description = "Check whether or not a given user is currently opted in to the game."
    )
    public Mono<Void> isToggledCommand(
            @CommandEvent ChatInputInteractionEvent event,
            @Option(description = "The user you want to check.") User user
    ) {
        return event.reply()
                .withContent("Frog");
    }
}
