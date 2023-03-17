package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;
import stelitop.dudesbot.game.entities.UserProfile;

@Component
public class IsToggledCommand implements ISlashCommand {

    @Autowired
    private UserProfileService userProfileService;

    @Override
    public String[] getNames() {
        return new String[]{"istoggled"};
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
        User user = options.getOption("user").get().getValue().get().asUser().block();
        var profile = userProfileService.getUserProfile(user.getId().asLong());
        if (profile.isParticipating()) {
            return event.reply()
                    .withContent(user.getUsername() + " has opted in for the bot.");
        } else {
            return event.reply()
                    .withContent(user.getUsername() + " has not opted in for the bot.");
        }
    }
}
