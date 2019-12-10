package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import hackaton.fastdisision.service.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Voting getByVoteId(@PathVariable("id") Voting voting) {
        return voting;
    }

    @GetMapping
    @JsonView(VotingView.FullData.class)
    public Iterable<Voting> getAllVotings() {
        return votingService.getAllVotings();
    }

    @PostMapping
    public Voting addVoting(@RequestBody Voting voting) {
        Voting newVoting = votingService.addVoting(voting);
        return newVoting;
    }



}
