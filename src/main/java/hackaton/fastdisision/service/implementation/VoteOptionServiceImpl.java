package hackaton.fastdisision.service.implementation;

import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import hackaton.fastdisision.service.intrface.VoteOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@Service
public class VoteOptionServiceImpl implements VoteOptionService {

    protected final VoteOptionRepo voteOptionRepo;

    @Autowired
    public VoteOptionServiceImpl(VoteOptionRepo voteOptionRepo, VotingRepo votingRepo) {
        this.voteOptionRepo = voteOptionRepo;
    }

    @Override
    public VoteOption acceptVote(VoteOption voteOption) {
        voteOption.addVote();
        voteOptionRepo.save(voteOption);
        return voteOption;
    }

}
