package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
public class AccessDeniedException extends ResponseStatusException {

    public AccessDeniedException() {
        super(HttpStatus.FORBIDDEN, "You have not enough permissions for this action!");
    }

    public AccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
