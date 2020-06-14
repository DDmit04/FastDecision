package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
public class NotFoundException extends ResponseStatusException {

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "Voting not found!");
    }

    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
