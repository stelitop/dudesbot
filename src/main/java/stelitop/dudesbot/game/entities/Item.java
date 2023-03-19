package stelitop.dudesbot.game.entities;

import jakarta.persistence.*;
import lombok.*;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Item")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {

    /**
     * The name of the item.
     */
    @Id
    private String name;

    /**
     * The text of the item.
     */
    @Column(length = 500)
    private String text;

    /**
     * The rarity of the item. Affects appearance rates.
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Rarity rarity;

    /**
     * The locations in which this item can appear. These are the IDs of the
     * channels it can appear. Empty list means it can appear everywhere.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Singular
    private List<Long> locations;

    /**
     * The flavor text of the item. Has no impact on gameplay.
     */
    @Column
    private String flavorText;

    /**
     * The users that own this dude.
     */
    @ManyToMany(mappedBy = "ownedItems", fetch = FetchType.EAGER)
    private Set<UserProfile> usersThatOwn;
}
