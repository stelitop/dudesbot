package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.DudeService;
import stelitop.dudesbot.discord.utils.EmojiUtils;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.enums.DudeStat;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DudeInfoCommand implements ISlashCommand {

    @Autowired
    private DudeService dudeService;

    @Autowired
    private EmojiUtils emojiUtils;

    @Override
    public String[] getNames() {
        return "dude".split(" ");
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
        //var nameOption = event.getOption("name");
        var nameOption = options.getOption("name");
        //var idOption = event.getOption("id");
        var idOption = options.getOption("id");

        Optional<Dude> dude;
        if (nameOption.isPresent() && nameOption.get().getValue().isPresent()) {
            dude = dudeService.getDude(nameOption.get().getValue().get().asString());
        } else if (idOption.isPresent() && idOption.get().getValue().isPresent()) {
            dude = dudeService.getDude(idOption.get().getValue().get().asLong());
        } else {
            return event.reply()
                    .withContent("Please provide at least one option!")
                    .withEphemeral(true);
        }

        if (dude.isEmpty()) {
            return event.reply()
                    .withContent("No dude matches this option!")
                    .withEphemeral(true);
        }

        return createDudeEmbed(dude.get(), event);
    }

    /**
     * Creates an embed message with the information about a given dude
     * and sends it as a reply to the event.
     *
     * @param dude The dude to show.
     * @return Mono void of the message.
     */
    private Mono<Void> createDudeEmbed(Dude dude, ChatInputInteractionEvent event) {

        var movesMsg = dude.getMoves() == null || dude.getMoves().isEmpty() ? "(none)" : dude.getMoves()
                .stream()
                .map(move -> emojiUtils.getEnergyEmoji() + " x" + move.getEnergy() + " | " + move.getTypes()
                        .stream().map(emojiUtils::getEmojiString).collect(Collectors.joining(" ")) +
                        " " + move.getName() + ": " + move.getDescription())
                .collect(Collectors.joining("\n"));

        var traitsMsg = dude.getTraits() == null || dude.getTraits().isEmpty() ? "(none)" : dude.getTraits()
                .stream()
                .map(move -> move.getName() + ": " + move.getDescription())
                .collect(Collectors.joining("\n"));

        var locationsMsg = dude.getAppearanceChannels().isEmpty() ? "Everywhere" : dude.getAppearanceChannels().stream()
                .map(x -> "<#" + x + ">").collect(Collectors.joining(", "));

        var embed = EmbedCreateSpec.builder()
                .title(dude.getFormattedId() + " - " + dude.getName())
                .thumbnail(dude.getArtLink())
                .color(dude.getTypes().isEmpty() ? Color.BLACK : Color.of(
                        dude.getTypes().stream().map(emojiUtils::getColor)
                        .mapToInt(Color::getRGB).sum() / dude.getTypes().size()))
                .addField("Collection Info",
                        "Rarity: " + dude.getRarity() +
                        "\nLocations: " + locationsMsg,
                        false)
                .addField("Evolution Info",
                        "Stage: " + dude.getStage() +
                        "\nEvolves From: " + (dude.getPreviousEvolutions() != null && !dude.getPreviousEvolutions().isEmpty() ?
                                String.join(", ", dude.getPreviousEvolutions()) : "N/A") +
                        "\nEvolves Into: " + (dude.getNextEvolutions() != null && !dude.getNextEvolutions().isEmpty() ?
                                String.join(", ", dude.getNextEvolutions()) : "N/A"),
                        false)
                .addField("Type Info",
                        dude.getTypes().stream()
                                .map(type -> emojiUtils.getEmojiString(type) + " " + type.toString().toUpperCase())
                                .collect(Collectors.joining(" - ")) +
                                (dude.getResistances().isEmpty() ? "" : "\nResistant to " +
                                dude.getResistances().stream().map(x -> emojiUtils.getEmojiString(x) + " " + x.toString().toUpperCase())
                                                .collect(Collectors.joining(" - "))) +
                                (dude.getWeaknesses().isEmpty() ? "" : "\nWeak to " +
                                dude.getWeaknesses().stream().map(x -> emojiUtils.getEmojiString(x) + " " + x.toString().toUpperCase())
                                        .collect(Collectors.joining(" - "))),
                        false)
                .addField("Statistics",
                        emojiUtils.getEmojiString(DudeStat.Health) + " Health: " + dude.getHealth() +
                        "\n" + emojiUtils.getEmojiString(DudeStat.Speed) + " Speed: " + dude.getSpeed() +
                        "\n" + emojiUtils.getEmojiString(DudeStat.Offense) + " Offense: " + dude.getOffense() +
                        "\n" + emojiUtils.getEmojiString(DudeStat.Defence) + " Defense: " + dude.getDefense(),
                        false)
                .addField("Traits", traitsMsg, false)
                .addField("Moves", movesMsg, false)
                .footer("Art by " + dude.getArtistName(), null);

        return event.reply()
                .withEmbeds(embed.build());
    }
}
