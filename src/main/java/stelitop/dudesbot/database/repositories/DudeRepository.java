package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;

//@Repository
public interface DudeRepository extends CrudRepository<Dude, Long> {

    @Transactional
    List<Dude> findByNameIgnoreCase(String name);
    @Transactional
    List<Dude> findByRarity(Rarity rarity);
}
