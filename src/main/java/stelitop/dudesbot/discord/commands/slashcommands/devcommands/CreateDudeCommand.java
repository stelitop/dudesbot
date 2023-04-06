package stelitop.dudesbot.discord.commands.slashcommands.devcommands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.DudeService;
import stelitop.dudesbot.discord.commands.slashcommands.ISlashCommand;
import stelitop.dudesbot.discord.commands.slashcommands.SlashCommandOptions;
import stelitop.dudesbot.discord.enums.DiscordChannels;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Move;
import stelitop.dudesbot.game.entities.Trait;
import stelitop.dudesbot.game.enums.ElementalType;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class CreateDudeCommand implements ISlashCommand {

    @Autowired
    private DudeService dudeService;
    @Autowired
    private Random random;

    @Override
    public String[] getNames() {
        return "z_devcreate dudetemplate".split(" ");
    }

    @Override
    public Mono<Void> handle(@NotNull ChatInputInteractionEvent event, SlashCommandOptions options) {

        String name = "New Dude " + random.nextLong(1000000000);

        Dude newDude = Dude.builder()
                .name(name)
                .id(dudeService.getAllDudes().stream().mapToLong(Dude::getId).max().getAsLong() + 1L)
                .stage(1)
                .artistName("<Artist>")
                .artLink("https://t3.ftcdn.net/jpg/03/35/13/14/240_F_335131435_DrHIQjlOKlu3GCXtpFkIG1v0cGgM9vJC.jpg")
                .rarity(Rarity.Common)
                .build();

        dudeService.saveDude(newDude);

        long id = dudeService.getDude(name).get().getId();

        return event.reply("Template dude created with id = " + id).withEphemeral(true);
    }
}
