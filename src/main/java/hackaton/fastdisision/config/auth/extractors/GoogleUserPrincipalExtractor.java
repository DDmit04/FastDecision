package hackaton.fastdisision.config.auth.extractors;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.service.intrface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Google user extraction class
 *
 * @author Daniil Dmitrochenkov
 * @version 1.3
 **/

@Component
public class GoogleUserPrincipalExtractor implements PrincipalExtractor {

    protected final UserService userService;

    @Autowired
    public GoogleUserPrincipalExtractor(UserService userService) {
        this.userService = userService;
    }

    /**
     * Extract google OAuth response to user
     * @param map user data from oauth service
     * @return extracted and saved user
     * @see UserRepo
     */
    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String id = String.valueOf(map.get("sub"));
        User extractedUser = userService.findByIdOptional(id).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(id);
            newUser.setPassword(UUID.randomUUID().toString());
            newUser.getRoles().add(UserRole.USER);
            newUser.setUsername((String) map.get("name"));
            newUser.setEmail((String) map.get("email"));
            newUser.setUserPic((String) map.get("picture"));
            return newUser;
        });
        return userService.createUser(extractedUser);
    }
}
