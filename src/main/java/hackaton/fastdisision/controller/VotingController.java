package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.OptionRepo;
import hackaton.fastdisision.repo.VoteRepo;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voteApi/votings")
public class VotingController {

    private VoteRepo voteRepo;
    private OptionRepo optionRepo;


    @Autowired
    public VotingController(VoteRepo voteRepo, OptionRepo optionRepo) {
        this.optionRepo = optionRepo;
        this.voteRepo = voteRepo;
    }

    @GetMapping("{id}")
    @JsonView(VotingView.CoreData.class)
    public Voting getByVoteId(@PathVariable("id") Voting voting) {
        return voting;
    }

    @GetMapping
    public Iterable<Voting> getAllVotings() {
        return voteRepo.findAll();
    }

    @PostMapping
    public Voting addVoting(@RequestBody Voting voting) {
        for(VoteOption voteOption : voting.getVoteOptions()) {
            voteOption.setVoting(voting);
        }
        return voteRepo.save(voting);
    }



}
