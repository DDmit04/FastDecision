package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.VotingNotFoundException;
import hackaton.fastdisision.excaptions.WrongVotingKeyException;
import hackaton.fastdisision.service.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voteApi/votings")
public class VotingController {

    private VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("{id}")
    @JsonView(VotingView.CoreData.class)
    public Voting getVotingById(@PathVariable("id") Voting voting, @RequestParam(required = false, defaultValue = "public") String votingKey) throws VotingNotFoundException, WrongVotingKeyException {
        if (voting == null) {
            throw new VotingNotFoundException();
        }
        if (!voting.getVotingKey().equals(votingKey)) {
            throw new WrongVotingKeyException();
        }
        return voting;
    }

    @GetMapping("{id}/validation/key")
    public boolean validateVotingKey(@PathVariable("id") Voting voting, @RequestParam(required = false, defaultValue = "public") String votingKey) throws VotingNotFoundException {
        if (voting == null) {
            throw new VotingNotFoundException();
        } else {
            return voting.getVotingKey().equals(votingKey);
        }
    }

    @PostMapping
    @JsonView(VotingView.MinimalData.class)
    public Voting addVoting(@RequestBody Voting voting, @AuthenticationPrincipal User user) {
        Voting newVoting = votingService.addVoting(voting, user);
        return newVoting;
    }

    @DeleteMapping("{id}")
    public void deleteVoting(@PathVariable("id") Voting voting, @AuthenticationPrincipal User user) {
        votingService.deleteVoting(voting, user);
    }

}
