package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.VotingRepo;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voteApi/charts")
public class VotingCartsController {

    private VotingRepo votingRepo;

    @Autowired
    public VotingCartsController(VotingRepo votingRepo) {
        this.votingRepo = votingRepo;
    }

    @GetMapping("/newest")
    @JsonView(VotingView.MinimalData.class)
    public Iterable<Voting> get10Newest() {
        Iterable<Voting> votings = votingRepo.findTop10ByIsPrivateVotingOrderByCreationDateDesc(false);
        return votings;
    }

    @GetMapping("/popular")
    @JsonView(VotingView.MinimalData.class)
    public Iterable<Voting> get10Popular() {
        Iterable<Voting> votings = votingRepo.findTop10ByIsPrivateVotingOrderByTotalVotesDesc(false);
        return votings;
    }

}
