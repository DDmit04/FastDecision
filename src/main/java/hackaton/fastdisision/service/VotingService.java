package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<Voting> get10Popular(Pageable pageable) {
        return votingRepo.findByIsPrivateVotingOrderByTotalVotes(false, pageable);
    }

    public Page<Voting> get10Newest(Pageable pageable) {
        return votingRepo.findByIsPrivateVotingOrderByCreationDate(false, pageable);
    }

    public Page<Voting> getUserPublic(User user, Pageable pageable) {
        Page<Voting> votings = new PageImpl<Voting>(Collections.EMPTY_LIST, pageable, 0);
        if (user != null) {
            votings = votingRepo.findByOwner_IdAndIsPrivateVotingOrderByCreationDate(user.getId(), false, pageable);
        }
        return votings;
    }

    public Page<Voting> getUserPrivate(User user, Pageable pageable) {
        Page<Voting> votings = new PageImpl<Voting>(Collections.EMPTY_LIST, pageable, 0);
        if (user != null) {
            votings = votingRepo.findByOwner_IdAndIsPrivateVotingOrderByCreationDate(user.getId(), true, pageable);
        }
        return votings;
    }


    public void deleteVoting(Voting voting, User user) {
        if ((voting.getOwner() != null && voting.getOwner().equals(user)) || user.getRoles().contains(UserRoles.ADMIN)) {
            if(voting.getOwner() != null) {
                voting.getOwner().getUserVotings().remove(voting);
                userRepo.save(voting.getOwner());
            }
            votingRepo.delete(voting);
        }
    }

    public Page<Voting> searchVotings(String search, Pageable pageable) {
        return votingRepo.findByIsPrivateVotingAndVotingTitleLike(false, search, pageable);
    }
}
