package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repo to control user entities
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
public interface UserRepo extends CrudRepository<User, String> {

    /**
     * returns user by ID
     *
     * @param id searched user ID
     * @return user
     * @see User
     */
    Optional<User> findById(String id);

}
