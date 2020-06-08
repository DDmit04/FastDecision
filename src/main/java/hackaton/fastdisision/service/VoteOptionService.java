package hackaton.fastdisision.service;

import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.WrongVotingKeyException;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to manipulate voting options entities
 *
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@Service
public class VoteOptionService {

    private VoteOptionRepo voteOptionRepo;
    private VotingRepo votingRepo;

    @Autowired
    public VoteOptionService(VoteOptionRepo voteOptionRepo, VotingRepo votingRepo) {
        this.voteOptionRepo = voteOptionRepo;
        this.votingRepo = votingRepo;
    }

    /**
     * check vote request IP and voting key to accept vote
     *
     * @param optionId ID of voting option
     * @param votedIp IP which vote
     * @param votingKey key of voting
     * @return vote option by optionId (with accepted vote or not)
     * @throws WrongVotingKeyException voting key is wrong
     * @see VoteOption
     */
    public VoteOption acceptVote(Long optionId, String votedIp, String votingKey) throws WrongVotingKeyException {
        VoteOption voteOption = voteOptionRepo.findById(optionId).get();
        Voting voting = voteOption.getVoting();
        if (!votingKey.equals(voting.getVotingKey())) {
            throw new WrongVotingKeyException();
        }
        if (voting.isCheckingIpVoting() && voting.getVotedIps().indexOf(votedIp) == -1) {
            addVotedIp(voting, votedIp);
            addVote(voting, voteOption);
        } else if (!voting.isCheckingIpVoting()) {
            addVote(voting, voteOption);
        }
        return voteOption;
    }

    /**
     * add one vote to voting
     *
     * @param voting voting to add vote
     * @param voteOption voting option to add vote
     */
    private void addVote(Voting voting, VoteOption voteOption) {
        voting.setTotalVotes(voting.getTotalVotes() + 1);
        voteOption.setPluses(voteOption.getPluses() + 1);
        voteOptionRepo.save(voteOption);
        votingRepo.save(voting);
    }

    /**
     * add IP to voting voted IP array
     *
     * @param voting voting to add voted IP
     * @param votedIp voted IP
     */
    private void addVotedIp(Voting voting, String votedIp) {
        voting.getVotedIps().add(votedIp);
        votingRepo.save(voting);
    }

}
