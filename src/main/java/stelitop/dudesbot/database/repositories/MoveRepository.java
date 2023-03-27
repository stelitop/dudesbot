package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stelitop.dudesbot.game.entities.Move;

//@Repository
public interface MoveRepository extends CrudRepository<Move, String> {

}
