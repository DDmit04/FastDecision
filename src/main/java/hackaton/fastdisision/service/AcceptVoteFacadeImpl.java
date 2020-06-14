package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.NotFoundException;
import hackaton.fastdisision.excaptions.VoteException;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.service.intrface.VoteOptionService;
import hackaton.fastdisision.service.intrface.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 **/
@Service
public class AcceptVoteFacadeImpl implements AcceptVoteFacade {

    protected final VotingService votingService;
    protected final VoteOptionService voteOptionService;
    protected final VoteOptionRepo voteOptionRepo;

    @Autowired
    public AcceptVoteFacadeImpl(VotingService votingService, VoteOptionService voteOptionService, VoteOptionRepo voteOptionRepo) {
        this.votingService = votingService;
        this.voteOptionService = voteOptionService;
        this.voteOptionRepo = voteOptionRepo;
    }

    @Override
    public VoteOption acceptVote(User user, Long optionId, String votedIp, String votingKey) throws VotingAccessException, VoteException, NotFoundException {
        Optional<VoteOption> voteOptionOptional = voteOptionRepo.findById(optionId);
        VoteOption voteOption;
        if(voteOptionOptional.isPresent()) {
            voteOption = voteOptionOptional.get();
        } else {
            throw new NotFoundException("Voting option not found!");
        }
        Voting voting = voteOption.getVoting();
        voting.userTryAccessVoting(user, votingKey);
        doFacade(voting, voteOption, votedIp);
        return voteOption;
    }

    private void doFacade(Voting voting, VoteOption voteOption, String votedIp) {
        if (voting.isCheckingIpVoting() && voting.isIpAlreadyVote(votedIp)) {
            throw new VoteException("Your IP already vote in this voting!");
        } else if (voting.isCheckingIpVoting()) {
            acceptWithIpSave(voteOption, votedIp);
        } else {
            acceptVote(voteOption);
        }
    }

    private void acceptWithIpSave(VoteOption voteOption, String votedIp) {
        voteOptionService.acceptVote(voteOption);
        votingService.addVotedIp(voteOption.getVoting(), votedIp);
    }

    private void acceptVote(VoteOption voteOption) {
        voteOptionService.acceptVote(voteOption);
    }

}
