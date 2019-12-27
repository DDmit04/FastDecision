package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFindByUsername = userRepo.findByUsername(username);
        return userFindByUsername;
    }

    public Optional<User> findById(String id) {
        return userRepo.findById(id);
    }
}
