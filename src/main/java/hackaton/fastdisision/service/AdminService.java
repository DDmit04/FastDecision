package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.WrongAdminPasswordException;
import hackaton.fastdisision.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service to manipulate admin roles in app
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@Service
public class AdminService {

    @Value("${admin.password}")
    private String serverAdminPassword;

    private UserRepo userRepo;

    @Autowired
    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * check user request for get admin role
     * @param currentUser user to give admin
     * @param clientAdminPassword password for prove admin access
     * @return modified currentUser (with admin role or not)
     * @throws WrongAdminPasswordException clientAdminPassword is wrong
     * @see User
     */
    public User checkAdminRequest(User currentUser, String clientAdminPassword) throws WrongAdminPasswordException {
        boolean adminPasswordIsAccepted = serverAdminPassword.equals(clientAdminPassword);
        boolean currentUserIsAdmin = currentUser.getRoles().contains(UserRoles.ADMIN);
        if(adminPasswordIsAccepted && !currentUserIsAdmin) {
            currentUser.getRoles().add(UserRoles.ADMIN);
            currentUser = userRepo.save(currentUser);
        } else if(!adminPasswordIsAccepted) {
            throw new WrongAdminPasswordException();
        }
        return currentUser;
    }

    /**
     * check user request for give admin role to other user
     * @param userToGiveAdmin user to give admin
     * @param currentUser user giving admin role
     * @return modified userToGiveAdmin (with admin role or not)
     * @throws AccessDeniedException current user isn't admin or userToGiveAdmin is admin or userToGiveAdmin is currentUser
     * @see User
     */
    public User giveAdmin(User userToGiveAdmin, User currentUser) throws AccessDeniedException {
        boolean currentUserIsAdmin = currentUser.getRoles().contains(UserRoles.ADMIN);
        boolean userIsAdmin = userToGiveAdmin.getRoles().contains(UserRoles.ADMIN);
        boolean usersIsEquals = userToGiveAdmin.equals(currentUser);
        if(currentUserIsAdmin && !userIsAdmin && !usersIsEquals) {
            userToGiveAdmin.getRoles().add(UserRoles.ADMIN);
            userToGiveAdmin = userRepo.save(userToGiveAdmin);
        } else {
            throw new AccessDeniedException();
        }
        return userToGiveAdmin;
    }

    /**
     * check user request for remove admin role from other user
     * @param userToRemoveAdmin user to remove admin
     * @param currentUser user asked for remove admin role
     * @param clientAdminPassword password for prove admin access
     * @return modified userToRemoveAdmin (without admin role or not)
     * @throws AccessDeniedException currentUser isn't admin or userToRemoveAdmin is currentUser
     * @throws WrongAdminPasswordException clientAdminPassword is wrong
     * @see User
     */
    public User removeAdmin(User userToRemoveAdmin, User currentUser, String clientAdminPassword) throws AccessDeniedException, WrongAdminPasswordException {
        boolean adminPasswordIsAccepted = serverAdminPassword.equals(clientAdminPassword);
        boolean currentUserIsAdmin = currentUser.getRoles().contains(UserRoles.ADMIN);
        if(!userToRemoveAdmin.equals(currentUser) && adminPasswordIsAccepted && currentUserIsAdmin) {
            userToRemoveAdmin.getRoles().remove(UserRoles.ADMIN);
            if(!userToRemoveAdmin.getRoles().contains(UserRoles.USER)) {
                userToRemoveAdmin.getRoles().add(UserRoles.USER);
            }
            userRepo.save(userToRemoveAdmin);
        } else if(!adminPasswordIsAccepted) {
            throw new WrongAdminPasswordException();
        } else {
            throw new AccessDeniedException();
        }
        return userToRemoveAdmin;
    }
}
