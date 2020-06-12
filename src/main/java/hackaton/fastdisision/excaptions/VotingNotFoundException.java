package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
public class VotingNotFoundException extends ResponseStatusException {

    public VotingNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Voting not found!");
    }
}
