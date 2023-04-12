package stelitop.dudesbot.discord.listeners;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.common.utils.RandomUtils;
import stelitop.dudesbot.database.services.DudeService;
import stelitop.dudesbot.database.services.ItemService;
import stelitop.dudesbot.database.services.UserProfileService;
import stelitop.dudesbot.discord.enums.DiscordChannels;
import stelitop.dudesbot.discord.utils.ColorUtils;
import stelitop.dudesbot.discord.utils.EmojiUtils;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.entities.UserProfile;
import stelitop.dudesbot.game.enums.ElementalType;
import stelitop.dudesbot.game.enums.Rarity;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DropsMessageListener implements ApplicationRunner {

    @Autowired
    private GatewayDiscordClient client;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private Random random;
    @Autowired
    private RandomUtils randomUtils;
    @Autowired
    private DudeService dudeService;
    @Autowired
    private EmojiUtils emojiUtils;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Environment environment;
    @Autowired
    private GatewayDiscordClient gatewayDiscordClient;
    @Autowired
    private ColorUtils colorUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.on(MessageCreateEvent.class, event -> {
            // check if member can be found and is not a bot
            if (event.getMember().isEmpty() || event.getMember().get().getId().asLong() == client.getSelfId().asLong()) {
                return Mono.empty();
            }
            var member = event.getMember().get();
            var profile = userProfileService.getUserProfile(member.getId().asLong());
            // if the person has not opted in to get dudes, return.
            if (!profile.isParticipating()) {
                return Mono.empty();
            }

            boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));

            // check if it has been 20 seconds since the last msg (not in devmode)
             if (!devmode) {
                Date lastMsg = profile.getLastMessage();
                if (lastMsg != null && Instant.now().toEpochMilli() - lastMsg.toInstant().toEpochMilli() < 20 * 1000) {
                    return Mono.empty();
                }
            }

            // update the timestamp
            profile.setLastMessage(Date.from(Instant.now()));
            var dmChannel = event.getMember().get().getPrivateChannel().block();
            var serverChannel = event.getMessage().getChannel().block();
            long channelId = serverChannel.getId().asLong();

            // pick the channel the msg originates from, unless its venting
            var notifChannel = channelId == DiscordChannels.VENTING ? dmChannel : serverChannel;

            var result = rollForDude(profile, channelId, notifChannel);
            if (result == null) {
                result = rollForItem(profile, channelId, notifChannel);
            }
            userProfileService.saveUserProfile(profile);
            return result == null ? Mono.empty() : result;
        }).subscribe();
    }

    /**
     * Rolls for an attempt to give the user a dude.
     *
     * @param profile The profile of the user.
     * @param originalChannelId The id of the channel the message was sent in.
     * @param notificationChannel The channel in which to send the drops message if
     *                            a dude is successfully obtained.
     * @return A message mono for the drops message in the specified channel if a
     *         dude was successfully obtained. If there was no dude obtained, null
     *         is returned.
     */
    private Mono<Message> rollForDude(UserProfile profile, long originalChannelId, MessageChannel notificationChannel) {

        boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));
        int roll = random.nextInt(devmode ? 3 : 100);
        if (roll != 0) {
            return null;
        }

        // verify the dude is not owned and available
        var ownedDudes = profile.getOwnedDudes().stream()
                .map(Dude::getName)
                .collect(Collectors.toSet());

        List<Dude> possibleDudes = dudeService.getAllDudes().stream()
                .filter(x -> !ownedDudes.contains(x.getName()))
                .filter(x -> x.getPreviousEvolutions() == null || x.getPreviousEvolutions().isEmpty() ||
                        x.getPreviousEvolutions().stream().anyMatch(ownedDudes::contains))
                //.filter(x -> x.getLocations().isEmpty() || x.getLocations().contains(originalChannelId) || devmode)
                .filter(x -> x.getLocations().contains(originalChannelId) || devmode)
                .toList();

        Rarity rarity = randomUtils.pickWeightedRandomRarity();

        while (true) {
            Rarity finalRarity = rarity;
            if (possibleDudes.stream().anyMatch(x -> x.getRarity() == finalRarity)) break;
            rarity = Rarity.downgradeRarity(rarity);
            if (rarity == Rarity.None) return null;
        }
        Rarity finalRarity = rarity;
        possibleDudes = possibleDudes.stream().filter(x -> x.getRarity() == finalRarity).toList();

        if (possibleDudes.isEmpty()) {
            return null;
        }

        // pick and add the dude
        Dude newDude = possibleDudes.get(random.nextInt(possibleDudes.size()));
        profile.getOwnedDudes().add(newDude);
        String username = gatewayDiscordClient.getUserById(Snowflake.of(profile.getDiscordId())).block().getUsername();
        String embedTitle = username + " found a " + newDude.getRarity().name().toLowerCase(Locale.ROOT) + " Dude!";
        if (newDude.getRarity() == Rarity.Epic) embedTitle = username + " found an epic Dude!";
        return notificationChannel.createMessage(EmbedCreateSpec.builder()
                .title(embedTitle)
                .description(newDude.getName() + " has been added to their collection.")
                .thumbnail(newDude.getArtLink())
                .color(colorUtils.getColor(newDude.getTypes()))
                .build());
    }

    private Mono<Message> rollForItem(UserProfile profile, long originalChannelId, MessageChannel notificationChannel) {

        boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));
        int roll = random.nextInt(devmode ? 6 : 200);
        if (roll != 0) {
            return null;
        }

        // verify the item is not owned and available
        var ownedItems = profile.getOwnedItems().stream()
                .map(Item::getName)
                .collect(Collectors.toSet());

        List<Item> possibleItems = itemService.getAllItems().stream()
                .filter(x -> !ownedItems.contains(x.getName()))
                //.filter(x -> x.getLocations().isEmpty() || x.getLocations().contains(originalChannelId) || devmode)
                .filter(x -> x.getLocations().contains(originalChannelId) || devmode)
                .toList();

        Rarity rarity = randomUtils.pickWeightedRandomRarity();

        while (true) {
            Rarity finalRarity = rarity;
            if (possibleItems.stream().anyMatch(x -> x.getRarity() == finalRarity)) break;
            rarity = Rarity.downgradeRarity(rarity);
            if (rarity == Rarity.None) return null;
        }
        Rarity finalRarity = rarity;
        possibleItems = possibleItems.stream().filter(x -> x.getRarity() == finalRarity).toList();

        if (possibleItems.isEmpty()) {
            return null;
        }

        // pick and add the item
        Item newItem = possibleItems.get(random.nextInt(possibleItems.size()));
        profile.getOwnedItems().add(newItem);
        String username = gatewayDiscordClient.getUserById(Snowflake.of(profile.getDiscordId())).block().getUsername();
        String embedTitle = username + " found a " + newItem.getRarity().name().toLowerCase(Locale.ROOT) + " item!";
        if (newItem.getRarity() == Rarity.Epic) embedTitle = username + " found an epic item!";
        return notificationChannel.createMessage(EmbedCreateSpec.builder()
                .title(embedTitle)
                .description(newItem.getName() + " has been added to their collection.")
                .color(colorUtils.getColor(ElementalType.Neutral))
                .build());
    }
}
