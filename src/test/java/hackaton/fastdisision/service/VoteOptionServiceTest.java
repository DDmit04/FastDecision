package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.service.intrface.VoteOptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 */
class VoteOptionServiceTest extends BasicTest {

    @Autowired
    private VoteOptionService voteOptionService;

    @MockBean
    private VoteOptionRepo voteOptionRepo;

    private static Voting inspectedVoting = new Voting();
    private static VoteOption firstVoteOption = new VoteOption();


    @BeforeEach
    public void initData() {

        inspectedVoting = new Voting();
        firstVoteOption = new VoteOption();

        inspectedVoting.setId((long) 1);

        firstVoteOption.setId((long) 2);
        firstVoteOption.setVoting(inspectedVoting);

        inspectedVoting.setVotingOptions(new ArrayList<>(Arrays.asList(firstVoteOption)));
    }

    @Test
    void acceptVote() throws VotingAccessException {

        inspectedVoting.setCheckingIpVoting(false);

        VoteOption voteOption = inspectedVoting.getVotingOptions().get(0);

        voteOptionService.acceptVote(voteOption);

        Mockito.verify(voteOptionRepo, times(1)).save(voteOption);
        assertEquals(1, voteOption.getPluses(), "total pluses is not incremented correctly!");
    }
}