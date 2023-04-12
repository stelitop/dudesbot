package stelitop.dudesbot.discord.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandInteractionOptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.discord.commands.slashcommands.ISlashCommand;
import stelitop.dudesbot.discord.commands.slashcommands.SlashCommandOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class SlashCommandListener implements ApplicationRunner {

    @Autowired
    private Collection<ISlashCommand> commands;
    @Autowired
    private GatewayDiscordClient client;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
    }

    /**
     * Entry point for all slash command interactions. When a slash
     * command enters it is redirected to one with a matching name.
     *
     * @param event Chat input interaction event
     * @return The reply to the command.
     */
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        ISlashCommand matchCommand = null;
        List<ApplicationCommandInteractionOption> commandOptions = null;

        if (event.getCommandName().startsWith("z_dev")) {
            long userId = event.getInteraction().getUser().getId().asLong();
            // TODO Move this to a separate class
            Set<Long> adminIds = Set.of(
                    237264833433567233L,
                    143170928623353856L
            );
            if (!adminIds.contains(userId)) {
                return event.reply("You are not authorised to use this command!")
                        .withEphemeral(true);
            }
        }

        for (var command : commands) {
            var names = command.getNames();
            if (names.length == 0) continue; // invalid command names
            if (!names[0].equals(event.getCommandName())) continue; // first name doesn't match
            //if (event.getOptions().size() < names.length - 1) continue; // not enough options to cover name

            var options = event.getOptions();
            boolean match = true;
            for (int i = 1; i < names.length; i++) {
                if (i == names.length - 1) {
                    if (options.isEmpty() || options.get(0).getType() != ApplicationCommandOption.Type.SUB_COMMAND || !options.get(0).getName().equals(names[i])) {
                        match = false;
                        break;
                    }
                }
                else {
                    if (options.isEmpty() || options.get(0).getType() != ApplicationCommandOption.Type.SUB_COMMAND_GROUP || !options.get(0).getName().equals(names[i])) {
                        match = false;
                        break;
                    }
                }
                options = options.get(0).getOptions();
            }

            if (!match) continue;

            if (matchCommand == null) { // first match
                matchCommand = command;
                commandOptions = options;
            } else { // should not happen normally
                System.err.println("Multiple commands matching " + String.join(" ", matchCommand.getNames()));
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Multiple command matches. This message should not be visible normally.");
            }
        }

        if (matchCommand != null) {
            try {
                return matchCommand.handle(event, new SlashCommandOptions(commandOptions));
            } catch (AssertionError e) {
                return event.reply()
                        .withEphemeral(true)
                        .withContent(e.getMessage());
            }
        } else {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("No matching commands found.");
        }
    }
}
