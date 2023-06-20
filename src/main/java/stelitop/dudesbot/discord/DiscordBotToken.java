package stelitop.dudesbot.discord;

import lombok.Getter;

public class DiscordBotToken {

    /**
     * Token for the discord bot. Should not be known.
     */
    @Getter
    private String token;

    /**
     * Discord ids of the admin users.
     */
    @Getter
    private long[] adminUsers;
}
