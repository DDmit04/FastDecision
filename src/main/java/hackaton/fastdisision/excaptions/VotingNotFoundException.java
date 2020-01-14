package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Voting not found!")
public class VotingNotFoundException extends Exception {
}
