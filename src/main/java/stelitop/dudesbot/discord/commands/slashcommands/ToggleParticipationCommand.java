package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;

@Component
public class ToggleParticipationCommand implements ISlashCommand {

    @Autowired
    private UserProfileService userProfileService;

    public static final long DUDES_DISCORD_ROLE_ID = 1093942085696110743L;
    public static final long ZEES_SERVER_ID = 568918715576352779L;

    @Override
    public String[] getNames() {
        return new String[]{"toggle"};
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
        User user = event.getInteraction().getUser();
        var guildId = event.getInteraction().getGuildId();
        Member member = null;
        if (guildId.isPresent() && guildId.get().asLong() == ZEES_SERVER_ID) {
            member = user.asMember(guildId.get()).block();
        }
        var profile = userProfileService.getUserProfile(user.getId().asLong());
        boolean curState = profile.isParticipating();
        if (!curState) {
            if (member != null) {
                try {
                    member.addRole(Snowflake.of(DUDES_DISCORD_ROLE_ID)).block();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            profile.setParticipating(true);
            userProfileService.saveUserProfile(profile);
            return event.reply()
                    .withContent("You are now participating in the game! You will now randomly collect Dudes when sending messages in the server. If you want to stop participating, use this command again.")
                    .withEphemeral(true);
        } else {
            if (member != null) {
                try {
                    member.removeRole(Snowflake.of(DUDES_DISCORD_ROLE_ID)).block();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            profile.setParticipating(false);
            userProfileService.saveUserProfile(profile);
            return event.reply()
                    .withContent("You are no longer participating in the game! You will no longer collect Dudes when sending messages in the server. If you want to participate, use this command again.")
                    .withEphemeral(true);
        }
    }
}
