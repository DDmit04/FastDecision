package hackaton.fastdisision.excaptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Wrong voting key!")
public class WrongVotingKeyException extends Exception {
}
