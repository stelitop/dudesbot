package stelitop.dudesbot.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import stelitop.dudesbot.game.entities.UserProfile;
import stelitop.dudesbot.database.repositories.UserProfileRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class UserProfileService {

    @Autowired
    transient UserProfileRepository userRepository;

    /**
     * Gets the profile of a user by their discord id. If the user
     * did not previously have an entry in the database, one is
     * created for them.
     *
     * @param id The id of the user.
     * @return The corresponding user profile.
     */
    public UserProfile getUserProfile(long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) return user.get();
        UserProfile newProfile = new UserProfile();
        newProfile.setDiscordId(id);
        newProfile.setLastMessage(null);
        userRepository.save(newProfile);
        return newProfile;
    }

    /**
     * Saves a profile entry into the database.
     *
     * @param profile The profile entity to save.
     */
    public void saveUserProfile(UserProfile profile) {
        userRepository.save(profile);
    }

    /**
     * Gets how many users have a profile in the database.
     *
     * @return The count of users.
     */
    public long getUserProfileCount() {
        return userRepository.count();
    }

    /**
     * Retrieves a list of all user profiles in the database.
     *
     * @return List of user profiles.
     */
    public List<UserProfile> getAllUserProfiles() {
        List<UserProfile> ret = new ArrayList<>();
        userRepository.findAll().forEach(ret::add);
        return ret;
    }
}
