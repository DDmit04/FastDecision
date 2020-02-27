package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.WrongAdminPasswordException;
import hackaton.fastdisision.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Value("${admin.password}")
    private String serverAdminPassword;

    private UserRepo userRepo;

    @Autowired
    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


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

    public User giveAdmin(User userToGiveAdmin) throws AccessDeniedException {
        boolean userIsAdmin = userToGiveAdmin.getRoles().contains(UserRoles.ADMIN);
        if(!userIsAdmin) {
            userToGiveAdmin.getRoles().add(UserRoles.ADMIN);
            userToGiveAdmin = userRepo.save(userToGiveAdmin);
        } else {
            throw new AccessDeniedException();
        }
        return userToGiveAdmin;
    }

    public User removeAdmin(User userToRemoveAdmin, User currentUser, String clientAdminPassword) throws AccessDeniedException, WrongAdminPasswordException {
        boolean adminPasswordIsAccepted = serverAdminPassword.equals(clientAdminPassword);
        boolean currentUserIsAdmin = currentUser.getRoles().contains(UserRoles.ADMIN);
        if(!userToRemoveAdmin.equals(currentUser) && adminPasswordIsAccepted && currentUserIsAdmin) {
            userToRemoveAdmin.getRoles().remove(UserRoles.ADMIN);
            userRepo.save(userToRemoveAdmin);
        } else if(!adminPasswordIsAccepted) {
            throw new WrongAdminPasswordException();
        } else {
            throw new AccessDeniedException();
        }
        return userToRemoveAdmin;
    }
}
