package stelitop.dudesbot.discord.commands.slashcommands.annotations;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;
import stelitop.dudesbot.discord.commands.slashcommands.ISlashCommand;
import stelitop.dudesbot.discord.commands.slashcommands.SlashCommandOptions;
import stelitop.dudesbot.discord.utils.EmojiUtils;
import stelitop.dudesbot.game.entities.UserProfile;
import stelitop.dudesbot.game.enums.ElementalType;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

@Component
public class StatisticsCommand implements ISlashCommand {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private EmojiUtils emojiUtils;
    @Autowired
    private GatewayDiscordClient discordClient;

    @Override
    public String[] getNames() {
        return new String[]{"statistics"};
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
        var profiles = userProfileService.getAllUserProfiles();
        long totalCaughtDudes = profiles.stream().mapToInt(x -> x.getOwnedDudes().size()).sum();
        long registeredPlayers = profiles.size();
        long activePlayers = profiles.stream().filter(UserProfile::isParticipating).count();
        Function<Rarity, Long> rarityCounter = (rarity) -> profiles.stream()
                .mapToLong(x -> x.getOwnedDudes().stream().filter(y -> y.getRarity() == rarity).count()).sum();

        int mostDudes = profiles.stream().mapToInt(x -> x.getOwnedDudes().size()).max().orElseGet(() -> -1);
        String topDudesUsernames = profiles.stream()
                .filter(x -> x.getOwnedDudes().size() == mostDudes)
                .map(x -> discordClient.getUserById(Snowflake.of(x.getDiscordId())).block())
                .filter(Objects::nonNull)
                .map(User::getUsername)
                .collect(Collectors.joining(", "));

        return event.reply().withEmbeds(EmbedCreateSpec.builder()
                .title("Battle Dudes Statistics")
                .color(emojiUtils.getColor(ElementalType.Neutral))
                .addField("General Statistics",
                        "Active Players: " + activePlayers +
                        (mostDudes == -1 ? "" : "\nBiggest collection (" + mostDudes + "): " + topDudesUsernames), false)
                .addField("Dudes Statistics",
                        "Total Dudes Owned: " + totalCaughtDudes +
                        "\n- Common: " + rarityCounter.apply(Rarity.Common) +
                        "\n- Rare: " + rarityCounter.apply(Rarity.Rare) +
                        "\n- Epic: " + rarityCounter.apply(Rarity.Epic) +
                        "\n- Legendary: " + rarityCounter.apply(Rarity.Legendary) +
                        "\n- Mythic: " + rarityCounter.apply(Rarity.Mythic), false)
                .build());
    }
}
