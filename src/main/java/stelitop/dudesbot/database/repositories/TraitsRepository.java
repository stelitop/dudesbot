package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import stelitop.dudesbot.game.entities.Trait;

public interface TraitsRepository extends CrudRepository<Trait, String> {

}
