package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Interface for creating discord Slash Commands.
 */
public interface ISlashCommand {

    /**
     * Gets the name(s) of the slash command. Usually only one, but
     * if it's a command with many subcommands then there should be
     * multiple names, in the correct order.
     *
     * @return An array containing the names of the command.
     * Essentially a constant.
     */
    String[] getNames();

    /**
     * Executes the command and responds to the given effect. This is called
     * once the command's name has been matched to the one that was passed.
     *
     * @param event The interaction event object.
     * @param options The options at the final level of command nesting.
     * @return The reply to the slash command
     */
    Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options);
}
