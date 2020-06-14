package hackaton.fastdisision.service.implementation;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.excaptions.NotFoundException;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.service.intrface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.3
 **/
@Service
public class UserServiceImpl implements UserService {

    protected final UserRepo userRepo;
    protected final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addRoleToUser(User user, UserRole userRole) {
        if (!user.getRoles().contains(userRole)) {
            user.getRoles().add(userRole);
            userRepo.save(user);
        }
        return user;
    }

    @Override
    public User removeRoleFromUser(User user, UserRole userRole) {
        if (user.getRoles().contains(userRole)) {
            user.getRoles().remove(userRole);
            userRepo.save(user);
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(LocalDateTime.now(Clock.systemUTC()));
        return userRepo.save(user);
    }

    @Override
    public User findById(String id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    @Override
    public Optional<User> findByIdOptional(String id) {
        return userRepo.findById(id);
    }

}
