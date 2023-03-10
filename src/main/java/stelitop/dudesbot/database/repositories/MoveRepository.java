package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import stelitop.dudesbot.game.entities.Move;

public interface MoveRepository extends CrudRepository<Move, String> {
}
