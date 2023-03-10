package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;

@Component
public class ToggleParticipationCommand implements ISlashCommand {

    @Autowired
    private UserProfileService userProfileService;

    @Override
    public String[] getNames() {
        return new String[]{"toggle"};
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
        var profile = userProfileService.getUserProfile(event.getInteraction().getUser().getId().asLong());
        boolean curState = profile.isParticipating();
        if (!curState) {
            profile.setParticipating(true);
            userProfileService.saveUserProfile(profile);
            return event.reply()
                    .withContent("You are now participating in the game! You will now randomly collect Dudes when sending messages in the server. If you want to stop participating, use this command again.")
                    .withEphemeral(true);
        } else {
            profile.setParticipating(false);
            userProfileService.saveUserProfile(profile);
            return event.reply()
                    .withContent("You are no longer participating in the game! You will no longer collect Dudes when sending messages in the server. If you want to participate, use this command again.")
                    .withEphemeral(true);
        }
    }
}
