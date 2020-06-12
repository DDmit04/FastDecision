package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
public class VoteException extends ResponseStatusException {

    public VoteException() {
        super(HttpStatus.NOT_ACCEPTABLE, "Some problems with accepting your vote!");
    }

    public VoteException(String reason) {
        super(HttpStatus.NOT_ACCEPTABLE, reason);
    }
}
