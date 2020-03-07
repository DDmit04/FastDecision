package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, String> {

    User findByUsername(String username);

    Optional<User> findById(String Id);

}
