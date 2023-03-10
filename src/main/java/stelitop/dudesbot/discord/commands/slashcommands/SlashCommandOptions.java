package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.core.object.command.ApplicationCommandInteractionOption;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public class SlashCommandOptions {

    @Getter
    private final List<ApplicationCommandInteractionOption> options;

    public SlashCommandOptions(List<ApplicationCommandInteractionOption> options) {
        this.options = options;
    }

    /**
     * Gets an option by its name if present.
     *
     * @param name The name of the option.
     * @return An optional object of the option.
     */
    public Optional<ApplicationCommandInteractionOption> getOption(String name) {
        return options.stream().filter(x -> x.getName().equals(name)).findFirst();
    }

    /**
     * Checks whether a specific option is present. Equivalent to
     * getOption(name).isPresent();
     *
     * @param name The name of the option.
     * @return True if the option is present and false otherwise.
     */
    public boolean hasOption(String name) {
        return options.stream().anyMatch(x -> x.getName().equals(name));
    }

    /**
     * Gets the option at a given index. Identical to .getOptions().get(index)
     *
     * @param index The index of the option.
     * @return The interaction option at this index.
     */
    public ApplicationCommandInteractionOption get(int index) {
        return options.get(index);
    }
}
