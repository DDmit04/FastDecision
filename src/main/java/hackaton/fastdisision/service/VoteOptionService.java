package hackaton.fastdisision.service;

import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteOptionService {

    private VoteOptionRepo voteOptionRepo;
    private VotingRepo votingRepo;

    @Autowired
    public VoteOptionService(VoteOptionRepo voteOptionRepo, VotingRepo votingRepo) {
        this.voteOptionRepo = voteOptionRepo;
        this.votingRepo = votingRepo;
    }

    public VoteOption acceptVote(long optionId, String votedIp) {
        VoteOption voteOption = voteOptionRepo.findById(optionId).get();
        Voting voting = voteOption.getVoting();
        if(voting.getVotedIps().indexOf(votedIp) == -1) {
            addVotedIp(voting, votedIp);
            voteOption.setPluses(voteOption.getPluses() + 1);
        }
        return voteOptionRepo.save(voteOption);
    }

    private void addVotedIp(Voting voting, String votedIp) {
        voting.setTotalVotes(voting.getTotalVotes() + 1);
        voting.getVotedIps().add(votedIp);
        votingRepo.save(voting);
    }

}
