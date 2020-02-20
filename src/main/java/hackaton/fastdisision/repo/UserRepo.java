package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, String> {

    Optional<User> findById(String Id);

}
