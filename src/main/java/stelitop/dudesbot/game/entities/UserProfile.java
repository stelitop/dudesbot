package stelitop.dudesbot.game.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "UserProfile")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

    /**
     * The discord id of the user, also used as a key to for the user table.
     */
    @Id
    @Column(nullable = false, unique = true)
    private long discordId;

    /**
     * The time of the last message sent by the user. Used to prevent spamming
     * as a means of acquiring dudes.
     */
    @Column
    private Date lastMessage;

    /**
     * The dudes owned by this user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "OwnedDudes",
            joinColumns = @JoinColumn(name = "userProfileId"),
            inverseJoinColumns = @JoinColumn(name = "dudeId"))
    private List<Dude> ownedDudes = new ArrayList<>();

    /**
     * The items owned by this user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "OwnedItems",
            joinColumns = @JoinColumn(name = "userProfileId"),
            inverseJoinColumns = @JoinColumn(name = "itemId"))
    private List<Item> ownedItems = new ArrayList<>();

    /**
     * Whether the user can earn dudes or not. Toggleable by the user
     * depending on if they want to be a part of the bot.
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean participating = false;
}
