package stelitop.dudesbot.discord.commands.slashcommands.devcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.DudeService;
import stelitop.dudesbot.discord.commands.slashcommands.ISlashCommand;
import stelitop.dudesbot.discord.commands.slashcommands.SlashCommandOptions;
import stelitop.dudesbot.game.enums.ElementalType;
import stelitop.dudesbot.game.enums.Rarity;

@Configuration
public class UpdateDudeCommands {

    @Autowired
    private DudeService dudeService;

    @Component
    public class Artlink implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude artlink".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String newArtLink = options.getOption("newlink").get().getValue().get().asString();
            dude.setArtLink(newArtLink);
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the art link")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Artist implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude artist".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String newArtist = options.getOption("artist").get().getValue().get().asString();
            dude.setArtistName(newArtist);
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the artist")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Type implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude type".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            ElementalType type = ElementalType.valueOf(options.getOption("newtype").get().getValue().get().asString());
            if (dude.getTypes().contains(type)) {
                dude.getTypes().remove(type);
            } else {
                dude.getTypes().add(type);
            }
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the types")
                    .withEphemeral(true);
        }
    }

    @Component
    public class FlavorText implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude flavortext".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String newFlavorText = options.getOption("flavortext").get().getValue().get().asString();
            dude.setFlavorText(newFlavorText);
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the flavor text")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Location implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude location".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            var channel = options.getOption("channel").get().getValue().get().asChannel().block().getId().asLong();

            if (dude.getLocations().contains(channel)) {
                dude.getLocations().remove(channel);
            } else {
                dude.getLocations().add(channel);
            }

            dudeService.saveDude(dude);

            dudeService.saveDude(dude);
            return event.reply("Successfully updated the locations")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Name implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude name".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String newName = options.getOption("name").get().getValue().get().asString();
            if (dudeService.getDude(newName).isPresent()) {
                return event.reply("There is already a dude with this name!")
                        .withEphemeral(true);
            }
            dude.setName(newName);
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the name")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Stage implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude stage".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            long newStage = options.getOption("stage").get().getValue().get().asLong();
            if (newStage < 1 || newStage > 3) {
                return event.reply("The stage must be between 1 and 3!")
                        .withEphemeral(true);
            }
            dude.setStage((int)newStage);
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the stage")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Resistance implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude resistance".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            ElementalType type = ElementalType.valueOf(options.getOption("resistance").get().getValue().get().asString());
            if (dude.getResistances().contains(type)) {
                dude.getResistances().remove(type);
            } else {
                dude.getResistances().add(type);
            }
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the resistances")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Weakness implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude weakness".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            ElementalType type = ElementalType.valueOf(options.getOption("weakness").get().getValue().get().asString());
            if (dude.getWeaknesses().contains(type)) {
                dude.getWeaknesses().remove(type);
            } else {
                dude.getWeaknesses().add(type);
            }
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the weaknesses")
                    .withEphemeral(true);
        }
    }

    @Component
    public class RarityC implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude rarity".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            var rarity = Rarity.valueOf(options.getOption("rarity").get().getValue().get().asString());
            dude.setRarity(rarity);
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the rarity")
                    .withEphemeral(true);
        }
    }

    @Component
    public class NextEv implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude nextevolution".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String newEvo = options.getOption("evolution").get().getValue().get().asString();
            if (newEvo.equals(dude.getName())) {
                return event.reply("The dude cannot evolve from itself!")
                        .withEphemeral(true);
            }
            if (dudeService.getDude(newEvo).isEmpty()) {
                return event.reply("There is no dude with this name!")
                        .withEphemeral(true);
            }

            if (dude.getNextEvolutions().contains(newEvo)) {
                dude.getNextEvolutions().remove(newEvo);
            } else {
                dude.getNextEvolutions().add(newEvo);
            }
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the next evolutions")
                    .withEphemeral(true);
        }
    }

    @Component
    public class PrevEv implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude previousevolution".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String newEvo = options.getOption("evolution").get().getValue().get().asString();
            if (newEvo.equals(dude.getName())) {
                return event.reply("The dude cannot evolve from itself!")
                        .withEphemeral(true);
            }
            if (dudeService.getDude(newEvo).isEmpty()) {
                return event.reply("There is no dude with this name!")
                        .withEphemeral(true);
            }

            if (dude.getPreviousEvolutions().contains(newEvo)) {
                dude.getPreviousEvolutions().remove(newEvo);
            } else {
                dude.getPreviousEvolutions().add(newEvo);
            }
            dudeService.saveDude(dude);
            return event.reply("Successfully updated the previous evolutions")
                    .withEphemeral(true);
        }
    }

    @Component
    public class Stat implements ISlashCommand {

        @Override
        public String[] getNames() {
            return "z_devupdate dude stat".split(" ");
        }

        @Override
        public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {
            long dudeId = options.getOption("dudeid").get().getValue().get().asLong();
            var dudeOpt = dudeService.getDude(dudeId);
            if (dudeOpt.isEmpty()) {
                return event.reply("Id could not be found!")
                        .withEphemeral(true);
            }
            var dude = dudeOpt.get();
            String stat = options.getOption("stat").get().getValue().get().asString();
            int value = (int)options.getOption("value").get().getValue().get().asLong();
            if (value < 0) {
                return event.reply("The value should be positive!")
                        .withEphemeral(true);
            }

            switch (stat) {
                case "Health" -> dude.setHealth(value);
                case "Speed" -> dude.setSpeed(value);
                case "Offense" -> dude.setOffense(value);
                case "Defence" -> dude.setDefense(value);
                default -> {
                    return event.reply("Unknown stat.").withEphemeral(true);
                }
            }

            dudeService.saveDude(dude);
            return event.reply("Successfully updated the stat")
                    .withEphemeral(true);
        }
    }
}
