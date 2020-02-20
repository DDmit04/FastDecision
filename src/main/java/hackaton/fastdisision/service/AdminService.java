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
        boolean currentUserIsAdmin = currentUser != null && currentUser.getRoles().contains(UserRoles.ADMIN);
        if(adminPasswordIsAccepted && !currentUserIsAdmin) {
            currentUser.getRoles().add(UserRoles.ADMIN);
            currentUser = userRepo.save(currentUser);
        } else if(!adminPasswordIsAccepted) {
            throw new WrongAdminPasswordException();
        }
        return currentUser;
    }

    public User giveAdmin(User user, User currentUser) throws AccessDeniedException {
        boolean userIsAdmin = user.getRoles().contains(UserRoles.ADMIN);
        if(!user.equals(currentUser) && !userIsAdmin) {
            user.getRoles().add(UserRoles.ADMIN);
            user = userRepo.save(currentUser);
        } else {
            throw new AccessDeniedException();
        }
        return user;
    }

    public User removeAdmin(User user, User currentUser, String clientAdminPassword) throws AccessDeniedException, WrongAdminPasswordException {
        boolean adminPasswordIsAccepted = serverAdminPassword.equals(clientAdminPassword);
        if(!user.equals(currentUser) && adminPasswordIsAccepted) {
            user.getRoles().remove(UserRoles.ADMIN);
            userRepo.save(user);
        } else if(!adminPasswordIsAccepted) {
            throw new WrongAdminPasswordException();
        } else {
            throw new AccessDeniedException();
        }
        return user;
    }
}
