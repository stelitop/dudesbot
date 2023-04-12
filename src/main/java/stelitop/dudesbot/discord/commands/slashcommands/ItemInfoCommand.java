package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.repositories.ItemRepository;
import stelitop.dudesbot.database.services.ItemService;
import stelitop.dudesbot.discord.utils.ColorUtils;
import stelitop.dudesbot.discord.utils.EmojiUtils;
import stelitop.dudesbot.game.enums.DudeStat;
import stelitop.dudesbot.game.enums.ElementalType;

import java.util.stream.Collectors;

@Component
public class ItemInfoCommand implements ISlashCommand {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ColorUtils colorUtils;

    @Override
    public String[] getNames() {
        return "item".split(" ");
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
        var name = options.getOption("name").get().getValue().get().asString();
        var itemOpt = itemService.getItem(name);
        if (itemOpt.isEmpty()) {
            return event.reply()
                    .withContent("No item matches this name!")
                    .withEphemeral(true);
        }
        var item = itemOpt.get();

        return event.reply()
                .withEmbeds(EmbedCreateSpec.builder()
                        .title(item.getName())
                        .color(colorUtils.getColor(ElementalType.Neutral))
                        .addField("Collection Info",
                                "Locations: " + (item.getLocations().isEmpty() ? "None" :
                                        item.getLocations().stream().map(x -> "<#" + x + ">")
                                                .collect(Collectors.joining(", "))) +
                                "\nRarity: " + item.getRarity() +
                                "\nTotal Collected: " + item.getUsersThatOwn().size(), false)
                        .addField("Effect", item.getText() +
                                (item.getFlavorText() == null ? "" : "\n\n*" + item.getFlavorText() + "*"), false)
                        .build());
    }
}
