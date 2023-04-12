package stelitop.dudesbot.discord.commands.slashcommands.annotations;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.possible.Possible;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.discord.commands.slashcommands.SlashCommandOptions;

public class TestCommand {

    @SlashCommand(
            name = "istoggled",
            description = "Check whether or not a given user is currently opted in to the game."
    )
    public Mono<Void> isToggledCommand(
            @CommandEvent ChatInputInteractionEvent event,
            @Option(description = "The user you want to check.") User user
    ) {
        //qazApplicationCommandRequest.builder().description()
        return event.reply()
                .withContent("Frog");
    }
}
