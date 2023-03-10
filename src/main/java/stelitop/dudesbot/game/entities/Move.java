package stelitop.dudesbot.game.entities;


import jakarta.persistence.*;
import lombok.*;
import stelitop.dudesbot.game.enums.ElementalType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Move")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Move {

    /**
     * The name of the move.
     */
    @Id
    @Column(nullable = false)
    private String name;

    /**
     * The description of the move.
     */
    @Column(nullable = false, length = 500)
    private String description;

    /**
     * The elemental types of the move.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<ElementalType> types = new ArrayList<>();

    /**
     * The energy cost of using the move.
     */
    @Column(nullable = false)
    private int energy;
}
