package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class VotingService {

    private VotingRepo votingRepo;
    private UserRepo userRepo;

    @Autowired
    public VotingService(VotingRepo votingRepo, UserRepo userRepo) {
        this.votingRepo = votingRepo;
        this.userRepo = userRepo;
    }

    public Voting addVoting(Voting voting, User user) {
        Voting savedVoting = null;
        if(!voting.getVotingTitle().trim().equals("") && voting.getVotingOptions().size() >= 2) {
            for (VoteOption voteOption : voting.getVotingOptions()) {
                voteOption.setVoting(voting);
            }
            voting.setCreationDate(LocalDateTime.now(Clock.systemUTC()));
            if (user != null) {
                voting.setOwner(user);
            }
            if(voting.isProtectedVoting()) {
                voting.setVotingKey(UUID.randomUUID().toString());
            } else {
                voting.setVotingKey("public");
            }
            savedVoting = votingRepo.save(voting);
        }
        return savedVoting;
    }

    public Iterable<Voting> get10Popular() {
        return votingRepo.findTop10ByIsPrivateVotingOrderByTotalVotesDesc(false);
    }

    public Iterable<Voting> get10Newest() {
        return votingRepo.findTop10ByIsPrivateVotingOrderByCreationDateDesc(false);
    }

    public Iterable<Voting> getUserPublic(User user) {
        Iterable<Voting> votings = Collections.EMPTY_LIST;
        if (user != null) {
            votings = votingRepo.findTop10ByOwner_IdAndIsPrivateVotingOrderByCreationDateDesc(user.getId(), false);
        }
        return votings;
    }

    public Iterable<Voting> getUserPrivate(User user) {
        Iterable<Voting> votings = Collections.EMPTY_LIST;
        if (user != null) {
            votings = votingRepo.findTop10ByOwner_IdAndIsPrivateVotingOrderByCreationDateDesc(user.getId(), true);
        }
        return votings;
    }


    public void deleteVoting(Voting voting, User user) {
        if (voting.getOwner().equals(user)) {
            user.getUserVotings().remove(voting);
            userRepo.save(user);
            votingRepo.delete(voting);
        }
    }
}
