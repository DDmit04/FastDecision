package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import hackaton.fastdisision.service.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voteApi/votings")
public class VotingController {

    private VotingService votingService;
    private VotingRepo votingRepo;

            @Autowired
    public VotingController(VotingService votingService, VotingRepo votingRepo) {
        this.votingService = votingService;
        this.votingRepo = votingRepo;
    }

    @GetMapping("{id}")
    @JsonView(VotingView.CoreData.class)
    public Voting getVotingById(@PathVariable("id") Voting voting) {
        return voting;
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
