package hackaton.fastdisision.service.implementation;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VotingDTO;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.NotFoundException;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.repo.VotingRepo;
import hackaton.fastdisision.service.intrface.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@Service
public class VotingServiceImpl implements VotingService {

    protected final VotingRepo votingRepo;
    protected final UserRepo userRepo;

    @Value("${voting.public.key}")
    private String publicVotingKey;

    @Autowired
    public VotingServiceImpl(VotingRepo votingRepo, UserRepo userRepo) {
        this.votingRepo = votingRepo;
        this.userRepo = userRepo;
    }

    @Override
    public VotingDTO addVoting(Voting voting, User user) {
        Voting savedVoting;
        voting.updateVotingOptionToVotingRelationship();
        voting.setCreationDate(LocalDateTime.now(Clock.systemUTC()));
        if (user != null) {
            voting.setOwner(user);
        }
        if (voting.isProtectedVoting()) {
            voting.setVotingKey(UUID.randomUUID().toString());
        } else {
            voting.setVotingKey(publicVotingKey);
        }
        savedVoting = votingRepo.save(voting);
        return new VotingDTO(savedVoting);
    }

    @Override
    public void deleteVoting(Voting voting, User user) throws AccessDeniedException {
        boolean userIsAdmin = user.isAdmin();
        boolean userIsOwner = voting.checkOwner(user);
        User votingOwner = voting.getOwner();
        if (userIsOwner || userIsAdmin) {
            votingOwner.deleteVotingRelationship(voting);
            userRepo.save(votingOwner);
            votingRepo.delete(voting);
        } else {
            throw new AccessDeniedException();
        }
    }

    @Override
    public Page<VotingDTO> getPopular(Pageable pageable) {
        return votingRepo.findByIsPrivateAndVoteOptionsPlusesSum(false, pageable);
    }

    @Override
    public Page<VotingDTO> getNewest(Pageable pageable) {
        return votingRepo.findByIsPrivateVotingOrderByCreationDate(false, pageable);
    }

    @Override
    public Page<VotingDTO> getUserPublic(User user, Pageable pageable) {
        Page<VotingDTO> votings = new PageImpl<VotingDTO>(Collections.EMPTY_LIST, pageable, 0);
        if (user != null) {
            votings = votingRepo.findByOwner_IdAndIsPrivateVotingOrderByCreationDate(user.getId(), false, pageable);
        }
        return votings;
    }

    @Override
    public Page<VotingDTO> getUserPrivate(User user, User currentUser, Pageable pageable) throws AccessDeniedException {
        Page<VotingDTO> votings = new PageImpl<VotingDTO>(Collections.EMPTY_LIST, pageable, 0);
        if (user != null && (user.equals(currentUser) || currentUser.isAdmin())) {
            votings = votingRepo.findByOwner_IdAndIsPrivateVotingOrderByCreationDate(user.getId(), true, pageable);
        } else {
            throw new AccessDeniedException("You can access this section!");
        }
        return votings;
    }

    @Override
    public Page<VotingDTO> searchVotings(String search, Pageable pageable) {
        return votingRepo.findAllByIsPrivateVotingAndVotingTitleContains(false, search, pageable);
    }

    @Override
    public VotingDTO findVotingDtoById(User user, String votingKey, long id) throws NotFoundException, VotingAccessException {
        Optional<Voting> voting = votingRepo.findById(id);
        if (!voting.isPresent()) {
            throw new NotFoundException();
        } else if (voting.get().userTryAccessVoting(user, votingKey)) {
            return new VotingDTO(voting.get());
        } else {
            throw new VotingAccessException(HttpStatus.NOT_ACCEPTABLE, "For some reasons you can not access this voting!");
        }
    }

    @Override
    public boolean validateVotingKey(Voting voting, String votingKey) throws NotFoundException {
        if (voting == null) {
            throw new NotFoundException();
        } else {
            return voting.keyMatches(votingKey);
        }
    }

    @Override
    public void addVotedIp(Voting voting, String votedIp) {
        voting.getVotedIps().add(votedIp);
        votingRepo.save(voting);
    }

}
