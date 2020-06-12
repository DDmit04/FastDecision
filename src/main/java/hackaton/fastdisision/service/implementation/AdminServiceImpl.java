package hackaton.fastdisision.service.implementation;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.service.intrface.AdminService;
import hackaton.fastdisision.service.intrface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Value("${admin.password}")
    private String serverAdminPassword;

    protected final UserService userService;

    @Autowired
    public AdminServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getAdminRole(User currentUser, String clientAdminPassword) throws AccessDeniedException {
        boolean adminPasswordIsAccepted = serverAdminPassword.equals(clientAdminPassword);
        if (adminPasswordIsAccepted) {
            currentUser = userService.addRoleToUser(currentUser, UserRole.ADMIN);
        } else {
            throw new AccessDeniedException();
        }
        return currentUser;
    }

    @Override
    public User giveAdminRole(User userToGiveAdmin, User currentUser) throws AccessDeniedException {
        boolean currentUserIsAdmin = currentUser.isAdmin();
        boolean usersAreEquals = userToGiveAdmin.equals(currentUser);
        if (currentUserIsAdmin && !usersAreEquals) {
            userToGiveAdmin = userService.addRoleToUser(userToGiveAdmin, UserRole.ADMIN);
        } else {
            throw new AccessDeniedException();
        }
        return userToGiveAdmin;
    }

    @Override
    public User removeAdminRole(User userToRemoveAdmin, User currentUser, String clientAdminPassword) throws AccessDeniedException {
        boolean adminPasswordIsAccepted = serverAdminPassword.equals(clientAdminPassword);
        boolean currentUserIsAdmin = currentUser.isAdmin();
        boolean usersAreEquals = userToRemoveAdmin.equals(currentUser);
        if (!usersAreEquals && adminPasswordIsAccepted && currentUserIsAdmin) {
            userToRemoveAdmin = userService.removeRoleFromUser(userToRemoveAdmin, UserRole.ADMIN);
        } else if (!adminPasswordIsAccepted) {
            throw new AccessDeniedException("Wrong admin password!");
        } else {
            throw new AccessDeniedException();
        }
        return userToRemoveAdmin;
    }
}
