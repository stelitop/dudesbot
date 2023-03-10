package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, String> {
    List<Item> findByNameIgnoreCase(String name);
    List<Item> findByRarity(Rarity rarity);
}
