package stelitop.dudesbot.game.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Trait")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trait {

    @Id
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Dude> dudesWithTrait;
}
