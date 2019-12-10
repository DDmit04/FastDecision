package hackaton.fastdisision.service;

import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.VoteOptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteOptionService {

    private VoteOptionRepo voteOptionRepo;

    @Autowired
    public VoteOptionService(VoteOptionRepo voteOptionRepo) {
        this.voteOptionRepo = voteOptionRepo;
    }

    public VoteOption acceptVote(long optionId, String userIp) {
        VoteOption voteOption = voteOptionRepo.findVoteOptionById(optionId);
        Voting voting = voteOption.getVoting();
        int ipIndex = 0;
        for(VoteOption voteOpt : voting.getVoteOptions()) {
            ipIndex = voteOpt.getVotedIps().indexOf(userIp);
            if(ipIndex != -1) {
                break;
            }
        }
        if(ipIndex == -1) {
            voteOption.getVotedIps().add(userIp);
            voteOption.setPluses(voteOption.getPluses() + 1);
            voting.setTotalVotes(voting.getTotalVotes() + 1);
        }
        return voteOptionRepo.save(voteOption);
    }

}
