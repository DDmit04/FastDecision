package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
public class VotingAccessException extends ResponseStatusException {

    public VotingAccessException() {
        super(HttpStatus.FORBIDDEN, "Some problems with access to this voting!");
    }

    public VotingAccessException(String reason) {
        super(HttpStatus.FORBIDDEN, reason);
    }

    public VotingAccessException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
