package stelitop.dudesbot.discord.utils;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import stelitop.dudesbot.game.enums.DudeStat;
import stelitop.dudesbot.game.enums.ElementalType;

@Service
public class EmojiUtils {

    /**
     * Transforms an elemental type into an emoji to draw.
     *
     * @param type The elemental type.
     * @return A string representing the emote.
     */
    public String getEmojiString(ElementalType type) {
        return switch (type) {
            case Air -> ":cloud:";
            case Fire -> ":fire:";
            case Earth -> ":bricks:";
            case Water -> ":bubbles:";
            case Tech -> ":nut_and_bolt:";
            case Magic -> ":crystal_ball:";
            case Decay -> ":bone:";
            case Nature -> ":herb:";
            case Neutral -> ":book:";
            default -> "";
        };
    }

    /**
     * Gets the emoji string of one of a dude's stats.
     *
     * @param stat The stat.
     * @return The emoji string. Empty if it doesn't match.
     */
    public String getEmojiString(DudeStat stat) {
        return switch (stat) {
            case Health -> ":heart:";
            case Speed -> ":clock3:";
            case Offense -> ":crossed_swords:";
            case Defence -> ":shield:";
            default -> "";
        };
    }

    /**
     * Gets the name of the emoji used for energy.
     *
     * @return The energy emoji.
     */
    public String getEnergyEmoji() {
        return ":zap:";
    }

    /**
     * Gets the colour of a given elemental type.
     *
     * @param elementalType The type.
     * @return The colour
     */
    public Color getColor(ElementalType elementalType) {
        return switch (elementalType) {
            case Air -> Color.of(150, 217, 214); // #96d9d6
            case Fire -> Color.of(238, 129, 48); // #ee8130
            case Earth -> Color.of(226, 191, 101); // #e2bf65
            case Water -> Color.of(99, 144, 240); // #6390f0
            case Tech -> Color.of(183, 183, 206); // #b7b7ce
            case Magic -> Color.of(214, 133, 173); // #d685ad
            case Decay -> Color.of(112, 87, 70); // #705746
            case Nature -> Color.of(122, 199, 76); // #7ac74c
            case Neutral -> Color.of(168, 167, 122); // #a8a77a
            default -> Color.BLACK;
        };
    }
}
