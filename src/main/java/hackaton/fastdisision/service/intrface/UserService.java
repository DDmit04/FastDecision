package hackaton.fastdisision.service.intrface;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;

import java.util.Optional;

/**
 * Service to control user entity
 *
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
public interface UserService {

    /**
     * Add new role to user
     *
     * @param user     user to add role
     * @param userRole role to add
     * @return user with new role added
     * @see User
     * @see UserRole
     */
    User addRoleToUser(User user, UserRole userRole);

    /**
     * Remove user role
     *
     * @param user     user to remove role
     * @param userRole role to remove
     * @return user without role
     * @see User
     * @see UserRole
     */
    User removeRoleFromUser(User user, UserRole userRole);

    /**
     * Create new user
     *
     * @param user user to save in DB
     * @return saved user
     * @see User
     */
    User createUser(User user);

    /**
     * Search user by is in DB
     *
     * @param id ID to search
     * @return search result
     * @see User
     */
    Optional<User> findById(String id);
}
