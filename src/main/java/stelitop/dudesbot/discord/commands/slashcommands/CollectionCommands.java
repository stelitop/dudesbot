package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.TextInput;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;
import stelitop.dudesbot.discord.listeners.IActionComponentListener;
import stelitop.dudesbot.discord.ui.CollectionUI;
import stelitop.dudesbot.discord.ui.CollectionUIManager;
import stelitop.dudesbot.discord.utils.EmojiUtils;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.enums.ElementalType;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@Configuration
public class CollectionCommands {

    /**
     * Types of ordering/sorting the entries of a collection.
     */
    public enum Ordering {
        Collected,
        Alphabetical,
        Id,
        Type
    }

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private EmojiUtils emojiUtils;
    @Autowired
    private CollectionUIManager collectionUIManager;

    /**
     * The constant id for this button.
     */
    private final String PREVIOUS_PAGE_BUTTON_ID = "collection_menu_previous_page_button";
    /**
     * The constant id for this button.
     */
    private final String NEXT_PAGE_BUTTON_ID = "collection_menu_next_page_button";

    /**
     * Updates the UI of a given message.
     *
     * @param message The message to update.
     */
    private void updateUserUI(Message message) {
        var uiOpt = collectionUIManager.getUI(message.getId().asLong());
        if (uiOpt.isEmpty()) return;
        var ui = uiOpt.get();

        var embed = EmbedCreateSpec.builder()
                .title(ui.getCollectionOwnerUsername() + "'s " + ui.getCollectionSubject())
                .color(emojiUtils.getColor(ElementalType.Neutral))
                .description("Owned: " + ui.getEntries().size() + "\n\n" +
                        String.join("\n", ui.getEntriesAtCurrentPage()) +
                        "\n\nPage " + ui.getCurrentPage() + "/" + ui.getTotalPages())
                .build();

        message.edit(MessageEditSpec.builder()
                .addEmbed(embed)
                .addComponent(ActionRow.of(
                        Button.primary(PREVIOUS_PAGE_BUTTON_ID, "Previous Page").disabled(ui.getCurrentPage() == 1),
                        Button.primary(NEXT_PAGE_BUTTON_ID, "Next Page").disabled(ui.getCurrentPage() == ui.getTotalPages())
                ))
                .build()).block();
    }

    @Component
    public class DudeCollectionCommand implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "collection dudes".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {

            User user = event.getInteraction().getUser();
            if (options.hasOption("user")) {
                user = options.getOption("user").get().getValue().get().asUser().block();
            }
            var profile = userProfileService.getUserProfile(user.getId().asLong());
            var dudes = profile.getOwnedDudes();
            var channel = event.getInteraction().getChannel().block();
            if (channel == null) return Mono.empty();

            if (options.hasOption("ordering")) {
                try {
                    Ordering order = Ordering.valueOf(options.getOption("ordering").get().getValue().get().asString());
                    switch (order) {
                        case Collected -> {}
                        case Id -> dudes.sort(Comparator.comparingLong(Dude::getId));
                        case Alphabetical -> dudes.sort(Comparator.comparing(Dude::getName));
                        case Type -> dudes.sort((o1, o2) -> {
                            for (int i = 0; i < o1.getTypes().size() && i < o2.getTypes().size(); i++) {
                                if (o1.getTypes().get(i) != o2.getTypes().get(i)) {
                                    return o1.getTypes().get(i).compareTo(o2.getTypes().get(i));
                                }
                            }
                            return Integer.compare(o1.getTypes().size(), o2.getTypes().size());
                        });
                    }
                } catch (IllegalArgumentException ignored) { }
            }

            if (options.getOption("reverse").isPresent()) {
                if (options.getOption("reverse").get().getValue().get().asBoolean()) {
                    Collections.reverse(dudes);
                }
            }

            event.reply().withEmbeds(EmbedCreateSpec.builder()
                    .title(user.getUsername() + "'s Dudes")
                    .description("Loading...")
                    .color(emojiUtils.getColor(ElementalType.Neutral))
                    .build()).block();

            Message message = event.getReply().block();
            collectionUIManager.saveUI(message.getId().asLong(), CollectionUI.builder()
                    .creatorId(event.getInteraction().getUser().getId().asLong())
                    .collectionOwnerUsername(user.getUsername())
                    .collectionSubject("Dudes")
                    .entries(dudes.stream()
                            .map(x -> x.getFormattedId() + " - " + x.getName() + " " + x.getTypes().stream()
                                    .map(emojiUtils::getEmojiString)
                                    .collect(Collectors.joining(" ")))
                            .toList())
                    .build());

            updateUserUI(message);
            return Mono.empty();
        }
    }

    /**
     * Slash command for creating an item collection menu.
     */
    @Component
    public class ItemCollectionCommand implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "collection items".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {

            User user = event.getInteraction().getUser();
            if (options.hasOption("user")) {
                user = options.getOption("user").get().getValue().get().asUser().block();
            }
            var profile = userProfileService.getUserProfile(user.getId().asLong());
            var items = profile.getOwnedItems();
            var channel = event.getInteraction().getChannel().block();
            if (channel == null) return Mono.empty();

            if (options.hasOption("ordering")) {
                try {
                    Ordering order = Ordering.valueOf(options.getOption("ordering").get().getValue().get().asString());
                    switch (order) {
                        case Collected -> {}
                        case Alphabetical -> items.sort(Comparator.comparing(Item::getName));
                        default -> {}
                    }
                } catch (IllegalArgumentException ignored) { }
            }

            if (options.getOption("reverse").isPresent()) {
                if (options.getOption("reverse").get().getValue().get().asBoolean()) {
                    Collections.reverse(items);
                }
            }

            event.reply().withEmbeds(EmbedCreateSpec.builder()
                    .title(user.getUsername() + "'s Items")
                    .description("Loading...")
                    .color(emojiUtils.getColor(ElementalType.Neutral))
                    .build()).block();

            Message message = event.getReply().block();
            collectionUIManager.saveUI(message.getId().asLong(), CollectionUI.builder()
                    .creatorId(event.getInteraction().getUser().getId().asLong())
                    .collectionOwnerUsername(user.getUsername())
                    .collectionSubject("Items")
                    .entries(items.stream()
                            .map(x -> x.getName() + " - " + x.getText())
                            .toList())
                    .build());

            updateUserUI(message);
            return Mono.empty();
        }
    }

    @Component
    public class PreviousPageButton implements IActionComponentListener<ButtonInteractionEvent> {

        @Override
        public String getComponentId() {
            return PREVIOUS_PAGE_BUTTON_ID;
        }

        @Override
        public Mono<Void> handle(ButtonInteractionEvent event) {
            if (event.getMessage().isEmpty()) {
                return event.reply()
                        .withContent("Could not update this collection.")
                        .withEphemeral(true);
            }
            var msg = event.getMessage().get();
            var uiOpt = collectionUIManager.getUI(msg.getId().asLong());
            if (uiOpt.isEmpty()) {
                return event.deferEdit();
            }
            var ui = uiOpt.get();
            if (ui.getCreatorId() != event.getInteraction().getUser().getId().asLong()) {
                return event.reply()
                        .withContent("This message was not created by you!")
                        .withEphemeral(true);
            }
            ui.previousPage();
            event.deferEdit().block();
            updateUserUI(msg);
            return Mono.empty();
        }
    }

    @Component
    public class NextPageButton implements IActionComponentListener<ButtonInteractionEvent> {

        @Override
        public String getComponentId() {
            return NEXT_PAGE_BUTTON_ID;
        }

        @Override
        public Mono<Void> handle(ButtonInteractionEvent event) {
            if (event.getMessage().isEmpty()) {
                return event.reply()
                        .withContent("Could not update this collection.")
                        .withEphemeral(true);
            }
            var msg = event.getMessage().get();
            var uiOpt = collectionUIManager.getUI(msg.getId().asLong());
            if (uiOpt.isEmpty()) {
                return event.deferEdit();
            }
            var ui = uiOpt.get();
            if (ui.getCreatorId() != event.getInteraction().getUser().getId().asLong()) {
                return event.reply()
                        .withContent("This message was not created by you!")
                        .withEphemeral(true);
            }
            ui.nextPage();
            event.deferEdit().block();
            updateUserUI(msg);
            return Mono.empty();
        }
    }
}
