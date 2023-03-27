package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stelitop.dudesbot.game.entities.Trait;

//@Repository
public interface TraitsRepository extends CrudRepository<Trait, String> {

}
