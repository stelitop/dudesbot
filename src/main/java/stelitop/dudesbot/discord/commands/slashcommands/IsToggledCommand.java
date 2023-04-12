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
        var userOption = options.getOption("user");
        User user;
        if (userOption.isEmpty()) {
            user = event.getInteraction().getUser();
        } else {
            user = userOption.get().getValue().get().asUser().block();
        }
        var profile = userProfileService.getUserProfile(user.getId().asLong());
        if (profile.isParticipating()) {
            return event.reply(user.getUsername() + " is currently opted in to the game. They will randomly collect Dudes and items when sending messages in this server.");
        } else {
            return event.reply(user.getUsername() + " is currently opted out of the game. They will not collect Dudes or items when sending messages in this server.");
        }
    }
}
