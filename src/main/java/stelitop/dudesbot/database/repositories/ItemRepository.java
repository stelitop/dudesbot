package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;

//@Repository
public interface ItemRepository extends CrudRepository<Item, String> {
    @Transactional
    List<Item> findByNameIgnoreCase(String name);
    @Transactional
    List<Item> findByRarity(Rarity rarity);
}
