package stelitop.dudesbot.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stelitop.dudesbot.database.repositories.DudeRepository;
import stelitop.dudesbot.database.repositories.MoveRepository;
import stelitop.dudesbot.database.repositories.TraitsRepository;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.enums.Rarity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class DudeService {

    @Autowired
    private DudeRepository dudeRepository;
    @Autowired
    private MoveRepository moveRepository;
    @Autowired
    private TraitsRepository traitsRepository;

    /**
     * Finds a dude in the repository by their unique name.
     *
     * @param name The name of the dude.
     * @return A dude object if there is such a dude and null
     * otherwise.
     */
    public Optional<Dude> getDude(String name) {
        var options = dudeRepository.findByNameIgnoreCase(name);
        if (options == null || options.isEmpty()) return Optional.empty();
        return Optional.of(options.get(0));
    }

    /**
     * Finds a dude in the repoitory by their unique id.
     *
     * @param id The id of the dude.
     * @return An optional object with a matching dude.
     */
    public Optional<Dude> getDude(long id) {
        return dudeRepository.findById(id);
    }

    /**
     * Saves a dude entity into the database, along with all their
     * traits and moves in their own respective databases
     *
     * @param dude Dude to save.
     */
    public void saveDudeWithMovesAndTraits(Dude dude) {
        moveRepository.saveAll(dude.getMoves());
        traitsRepository.saveAll(dude.getTraits());
        dudeRepository.save(dude);
    }

    /**
     * Gets all dudes from the database of a given rarity.
     *
     * @param rarity The rarity of the dudes.
     * @return A list of all dudes of that rarity.
     */
    public List<Dude> getDudesOfRarity(Rarity rarity) {
        return dudeRepository.findByRarity(rarity);
    }
}
