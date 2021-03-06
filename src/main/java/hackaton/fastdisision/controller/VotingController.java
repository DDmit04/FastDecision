package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VotingDTO;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.NotFoundException;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.service.intrface.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * Controller that handles requests for voting
 *
 * @author Dmitrochenkov Daniil
 * @version 1.3
 */
@RestController
@RequestMapping("/voteApi/votings")
public class VotingController {

    private final VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("{id}")
    @JsonView(VotingView.MinimalData.class)
    public VotingDTO getVotingById(@PathVariable("id") long id,
                                   @RequestParam(required = false, defaultValue = "${voting.public.key}") String votingKey,
                                   @AuthenticationPrincipal User currentUser) throws NotFoundException, VotingAccessException {
        return votingService.findVotingDtoById(currentUser, votingKey, id);
    }

    @GetMapping("{id}/validation/key")
    public Map<String, Boolean> validateVotingKey(@PathVariable("id") Voting voting,
                                                  @RequestParam(required = false, defaultValue = "${voting.public.key}") String votingKey) throws NotFoundException {
        boolean keyIsValid = votingService.validateVotingKey(voting, votingKey);
        return Collections.singletonMap("keyIsValid", keyIsValid);
    }

    @PostMapping
    @JsonView(VotingView.CoreData.class)
    public VotingDTO addVoting(@Valid @RequestBody Voting voting,
                               @AuthenticationPrincipal User user) {
        return votingService.addVoting(voting, user);
    }

    @DeleteMapping("{id}")
    public void deleteVoting(@PathVariable("id") Voting voting,
                             @AuthenticationPrincipal User user) throws AccessDeniedException {
        votingService.deleteVoting(voting, user);
    }

}
