package stelitop.dudesbot.discord.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
import java.util.Objects;
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

            // check if it has been 20 seconds since the last msg
//            Date lastMsg = profile.getLastMessage();
//            if (lastMsg == null || Instant.now().toEpochMilli() - lastMsg.toInstant().toEpochMilli() < 20*1000) {
//                return Mono.empty();
//            }

            // check if the channel is accessible
            // TODO change it when there is a dedicated channel
            var channel = event.getMessage().getChannel().block();
            if (channel == null) {
                return Mono.empty();
            }

            // update the timestamp
            profile.setLastMessage(Date.from(Instant.now()));

            var result = rollForDude(profile, channel);
            if (result == null) {
                result = rollForItem(profile, channel);
            }
            userProfileService.saveUserProfile(profile);
            return result == null ? Mono.empty() : result;
        }).subscribe();
    }

    private Mono<Message> rollForDude(UserProfile profile, MessageChannel channel) {

        //if (random.nextInt(100) != 0) {
        if (random.nextInt(3) != 0) {
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
                .toList();
        if (possibleDudes.isEmpty()) {
            return Mono.empty();
        }

        // pick and add the dude
        Dude newDude = possibleDudes.get(random.nextInt(possibleDudes.size()));
        profile.getOwnedDudes().add(newDude);
        return channel.createMessage(EmbedCreateSpec.builder()
                .title("You found a Dude!")
                .description(newDude.getName() + " has been added to your collection.")
                .thumbnail(newDude.getArtLink())
                .color(emojiUtils.getColor(ElementalType.Neutral))
                .build());
    }

    private Mono<Message> rollForItem(UserProfile profile, MessageChannel channel) {

        //if (random.nextInt(250) != 0) {
        if (random.nextInt(6) != 0) {
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
                .toList();
        if (possibleItems.isEmpty()) {
            return Mono.empty();
        }

        // pick and add the item
        Item newItem = possibleItems.get(random.nextInt(possibleItems.size()));
        profile.getOwnedItems().add(newItem);
        return channel.createMessage(EmbedCreateSpec.builder()
                .title("You found an item!")
                .description(newItem.getName() + " has been added to your collection.")
                .color(emojiUtils.getColor(ElementalType.Neutral))
                .build());
    }
}
