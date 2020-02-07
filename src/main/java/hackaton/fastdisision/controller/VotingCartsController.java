package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.service.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voteApi/charts")
public class VotingCartsController {

    private VotingService votingService;

    @Autowired
    public VotingCartsController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("/newest")
    @JsonView(VotingView.MinimalData.class)
    public Iterable<Voting> get10Newest() {
        Iterable<Voting> votings = votingService.get10Newest();
        return votings;
    }

    @GetMapping("/popular")
    @JsonView(VotingView.MinimalData.class)
    public Iterable<Voting> get10Popular() {
        Iterable<Voting> votings = votingService.get10Popular();
        return votings;
    }

    @GetMapping("/userPublic/{id}")
    @JsonView(VotingView.MinimalData.class)
    public Iterable<Voting> getUserPublic(@PathVariable("id") User user) {
        Iterable<Voting> votings = votingService.getUserPublic(user);
        return votings;
    }

    @GetMapping("/userPrivate/{id}")
    @JsonView(VotingView.MinimalData.class)
    public Iterable<Voting> getUserPrivate(@PathVariable("id") User user) {
        Iterable<Voting> votings = votingService.getUserPrivate(user);
        return votings;
    }

}
