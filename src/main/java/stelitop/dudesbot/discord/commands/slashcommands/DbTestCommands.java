package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;

@Configuration
public class DbTestCommands {

    @Autowired
    private UserProfileService userProfileService;

    @Component
    public class UserCount implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "dbtest usercount".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            return event.reply()
                    .withContent("There are " + userProfileService.getUserProfileCount() + " entries in the profile database.")
                    .withEphemeral(true);
        }
    }

    @Component
    public class UserInfo implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "dbtest getuser".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            String msg = "";
            long id = options.get(0).getValue().get().asLong();
            var profile = userProfileService.getUserProfile(id);
            return event.reply()
                    .withContent("Frog " + profile.getDiscordId())
                    .withEphemeral(true);
        }
    }
}
