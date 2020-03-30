package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.VotingNotFoundException;
import hackaton.fastdisision.excaptions.WrongVotingKeyException;
import hackaton.fastdisision.service.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * Controller that handles requests for voting
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@RestController
@RequestMapping("/voteApi/votings")
public class VotingController {

    @Value("${voting.public.key}")
    private String publicVotingKey;

    private VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("{id}")
    @JsonView(VotingView.MinimalData.class)
    public Voting getVotingById(@PathVariable("id") Voting voting,
                                @RequestParam(required = false, defaultValue = "public") String votingKey,
                                @AuthenticationPrincipal User currentUser) throws VotingNotFoundException, WrongVotingKeyException {
        if (voting == null) {
            throw new VotingNotFoundException();
        }
        if(currentUser == null || !currentUser.equals(voting.getOwner())) {
            if (!voting.getVotingKey().equals(votingKey)) {
                throw new WrongVotingKeyException();
            }
        }
        return voting;
    }

    @GetMapping("{id}/validation/key")
    public Map<String, Boolean> validateVotingKey(@PathVariable("id") Voting voting,
                                                  @RequestParam(required = false, defaultValue = "${voting.public.key}") String votingKey) throws VotingNotFoundException {
        if (voting == null) {
            throw new VotingNotFoundException();
        } else if(votingKey.equals(publicVotingKey)){
            return Collections.singletonMap("keyIsValid", true);
        } else {
            boolean keyIsValid = voting.getVotingKey().equals(votingKey);
            return Collections.singletonMap("keyIsValid", keyIsValid);
        }
    }

    @PostMapping
    @JsonView(VotingView.CoreData.class)
    public Voting addVoting(@Valid @RequestBody Voting voting,
                            @AuthenticationPrincipal User user) {
        Voting newVoting = votingService.addVoting(voting, user);
        return newVoting;
    }

    @DeleteMapping("{id}")
    public void deleteVoting(@PathVariable("id") Voting voting,
                             @AuthenticationPrincipal User user) throws AccessDeniedException {
        votingService.deleteVoting(voting, user);
    }

}
