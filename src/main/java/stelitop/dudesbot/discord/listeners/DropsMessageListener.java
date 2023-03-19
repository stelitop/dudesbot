package stelitop.dudesbot.discord.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.common.utils.RandomUtils;
import stelitop.dudesbot.database.services.DudeService;
import stelitop.dudesbot.database.services.ItemService;
import stelitop.dudesbot.database.services.UserProfileService;
import stelitop.dudesbot.discord.utils.EmojiUtils;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.entities.UserProfile;
import stelitop.dudesbot.game.enums.ElementalType;
import stelitop.dudesbot.game.enums.Rarity;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.on(MessageCreateEvent.class, event -> {
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

            // check if it has been 20 seconds since the last msg
            if (!devmode) {
                Date lastMsg = profile.getLastMessage();
                if (lastMsg == null || Instant.now().toEpochMilli() - lastMsg.toInstant().toEpochMilli() < 20 * 1000) {
                    return Mono.empty();
                }
            }

            // update the timestamp
            profile.setLastMessage(Date.from(Instant.now()));
            var dmChannel = event.getMember().get().getPrivateChannel().block();
            long channelId = event.getMessage().getChannel().block().getId().asLong();
            var result = rollForDude(profile, channelId, dmChannel);
            if (result == null) {
                result = rollForItem(profile, channelId, dmChannel);
            }
            userProfileService.saveUserProfile(profile);
            return result == null ? Mono.empty() : result;
        }).subscribe();
    }

    private Mono<Message> rollForDude(UserProfile profile, long originalChannlId, MessageChannel notificationChannel) {

        boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));

        if ((devmode && random.nextInt(3) != 0) || (!devmode && random.nextInt(100) != 0)) {
            return null;
        }

        Rarity rarity = randomUtils.pickRandomlyWithWeights(List.of(
                Pair.of(Rarity.Common, 25),
                Pair.of(Rarity.Rare, 16),
                Pair.of(Rarity.Epic, 9),
                Pair.of(Rarity.Legendary, 4),
                Pair.of(Rarity.Mythic, 1)
        ));

        // verify the dude is not owned and available
        var possibleDudes = dudeService.getDudesOfRarity(rarity);
        var ownedDudes = profile.getOwnedDudes().stream()
                .map(Dude::getName)
                .collect(Collectors.toSet());

        // TODO check if it's the correct channel
        possibleDudes = possibleDudes.stream()
                .filter(x -> !ownedDudes.contains(x.getName()))
                .filter(x -> x.getPreviousEvolutions() == null || x.getPreviousEvolutions().isEmpty() ||
                        x.getPreviousEvolutions().stream().anyMatch(ownedDudes::contains))
                .filter(x -> x.getLocations().isEmpty() || x.getLocations().contains(originalChannlId) || devmode)
                .toList();
        if (possibleDudes.isEmpty()) {
            return Mono.empty();
        }

        // pick and add the dude
        Dude newDude = possibleDudes.get(random.nextInt(possibleDudes.size()));
        profile.getOwnedDudes().add(newDude);
        return notificationChannel.createMessage(EmbedCreateSpec.builder()
                .title("You found a Dude!")
                .description(newDude.getName() + " has been added to your collection.")
                .thumbnail(newDude.getArtLink())
                .color(emojiUtils.getColor(ElementalType.Neutral))
                .build());
    }

    private Mono<Message> rollForItem(UserProfile profile, long originalChannlId, MessageChannel notificationChannel) {

        boolean devmode = Boolean.parseBoolean(environment.getProperty("devmode"));

        if ((devmode && random.nextInt(6) != 0) || (!devmode && random.nextInt(250) != 0)) {
            return null;
        }

        Rarity rarity = randomUtils.pickRandomlyWithWeights(List.of(
                Pair.of(Rarity.Common, 25),
                Pair.of(Rarity.Rare, 16),
                Pair.of(Rarity.Epic, 9),
                Pair.of(Rarity.Legendary, 4),
                Pair.of(Rarity.Mythic, 1)
        ));

        // verify the item is not owned and available
        var possibleItems = itemService.getItemsOfRarity(rarity);
        var ownedItems = profile.getOwnedItems().stream()
                .map(Item::getName)
                .collect(Collectors.toSet());

        // TODO check if it's the correct channel
        possibleItems = possibleItems.stream()
                .filter(x -> !ownedItems.contains(x.getName()))
                .filter(x -> x.getLocations().isEmpty() || x.getLocations().contains(originalChannlId) || devmode)
                .toList();
        if (possibleItems.isEmpty()) {
            return Mono.empty();
        }

        // pick and add the item
        Item newItem = possibleItems.get(random.nextInt(possibleItems.size()));
        profile.getOwnedItems().add(newItem);

        return notificationChannel.createMessage(EmbedCreateSpec.builder()
                .title("You found an item!")
                .description(newItem.getName() + " has been added to your collection.")
                .color(emojiUtils.getColor(ElementalType.Neutral))
                .build());
    }
}
