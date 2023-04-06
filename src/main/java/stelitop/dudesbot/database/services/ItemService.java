package stelitop.dudesbot.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stelitop.dudesbot.database.repositories.DudeRepository;
import stelitop.dudesbot.database.repositories.ItemRepository;
import stelitop.dudesbot.database.repositories.MoveRepository;
import stelitop.dudesbot.database.repositories.TraitsRepository;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Finds an item in the repository by their unique name. Case-insensitive.
     *
     * @param name The name of the item.
     * @return An item object if there is such a dude and null
     * otherwise.
     */
    public Optional<Item> getItem(String name) {
        var options = itemRepository.findByNameIgnoreCase(name);
        if (options == null || options.isEmpty()) return Optional.empty();
        return Optional.of(options.get(0));
    }

    /**
     * Saves an item entity into the database.
     *
     * @param item Item to save.
     */
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * Gets all items from the database of a given rarity.
     *
     * @param rarity The rarity of the items.
     * @return A list of all items of that rarity.
     */
    public List<Item> getItemsOfRarity(Rarity rarity) {
        return itemRepository.findByRarity(rarity);
    }

    /**
     * Gets all items currently in the repository.
     *
     * @return A mutable list containing all items.
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }
}
