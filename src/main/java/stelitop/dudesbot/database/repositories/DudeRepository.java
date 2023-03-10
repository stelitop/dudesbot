package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;

@Repository
public interface DudeRepository extends CrudRepository<Dude, Long> {
    List<Dude> findByNameIgnoreCase(String name);
    List<Dude> findByRarity(Rarity rarity);
}
