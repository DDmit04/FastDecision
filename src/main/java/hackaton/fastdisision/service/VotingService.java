package hackaton.fastdisision.service;

import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.VotingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class VotingService {

    private VotingRepo votingRepo;

    @Autowired
    public VotingService(VotingRepo votingRepo) {
        this.votingRepo = votingRepo;
    }

    public Voting addVoting(Voting voting) {
        for (VoteOption voteOption : voting.getVoteOptions()) {
            voteOption.setVoting(voting);
        }
        voting.setCreationDate(LocalDateTime.now(Clock.systemUTC()));
        return votingRepo.save(voting);
    }

    public Iterable<Voting> getAllVotings() {
        return votingRepo.findAll();
    }

}
