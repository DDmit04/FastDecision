package hackaton.fastdisision.controller;

import hackaton.fastdisision.data.Vote;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.OptionRepo;
import hackaton.fastdisision.repo.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voteApi/votings")
public class VoteController {

    private VoteRepo voteRepo;
    private OptionRepo optionRepo;


    @Autowired
    public VoteController(VoteRepo voteRepo, OptionRepo optionRepo) {
        this.optionRepo = optionRepo;
        this.voteRepo = voteRepo;
    }

    @GetMapping("{id}")
    public Vote getByVoteId(@PathVariable("id") Vote vote) {
        return vote;
    }

    @GetMapping
    public Iterable<Vote> getAllVotings() {
        return voteRepo.findAll();
    }

    @PostMapping
    public Vote addVoting(@RequestBody Vote vote) {
        for(VoteOption voteOption : vote.getVoteOptions()) {
            voteOption.setVote(vote);
        }
        return voteRepo.save(vote);
    }



}
