package hackaton.fastdisision.service.intrface;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.excaptions.AccessDeniedException;

/**
 * Service to manipulate admin roles in app
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
public interface AdminService {

    /**
     * check user request for get admin role
     *
     * @param currentUser         user to give admin
     * @param clientAdminPassword password for prove admin access
     * @return modified currentUser (with admin role or not)
     * @throws AccessDeniedException clientAdminPassword is wrong
     * @see User
     */
    User getAdminRole(User currentUser, String clientAdminPassword) throws AccessDeniedException;

    /**
     * check user request for give admin role to other user
     *
     * @param userToGiveAdmin user to give admin
     * @param currentUser     user giving admin role
     * @return modified userToGiveAdmin (with admin role or not)
     * @throws AccessDeniedException current user isn't admin or userToGiveAdmin is admin or userToGiveAdmin is currentUser
     * @see User
     */
    User giveAdminRole(User userToGiveAdmin, User currentUser) throws AccessDeniedException;

    /**
     * check user request for remove admin role from other user
     *
     * @param userToRemoveAdmin   user to remove admin
     * @param currentUser         user asked for remove admin role
     * @param clientAdminPassword password for prove admin access
     * @return modified userToRemoveAdmin (without admin role or not)
     * @throws AccessDeniedException       currentUser isn't admin or userToRemoveAdmin is currentUser or clientAdminPassword is wrong
     * @see User
     */
    User removeAdminRole(User userToRemoveAdmin, User currentUser, String clientAdminPassword) throws AccessDeniedException;
}
