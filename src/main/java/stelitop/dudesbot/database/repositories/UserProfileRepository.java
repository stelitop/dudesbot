package stelitop.dudesbot.database.repositories;

import org.springframework.data.repository.CrudRepository;
import stelitop.dudesbot.game.entities.UserProfile;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

}
