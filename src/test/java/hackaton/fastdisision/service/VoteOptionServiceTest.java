package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.WrongVotingKeyException;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class VoteOptionServiceTest extends BasicTest {

    @Autowired
    private VoteOptionService voteOptionService;

    @MockBean
    private VoteOptionRepo voteOptionRepo;

    @MockBean
    private VotingRepo votingRepo;

    private static Voting inspectedVoting = new Voting();
    private static VoteOption firstVoteOption = new VoteOption();
    private static VoteOption secVoteOption = new VoteOption();

    private String votedIp = "0.0.0.0.0";
    private static String rightVotingKey = "trueKey";
    private static String wrongVotingKey = "wrongKey";

    @BeforeAll
    public static void initData() {
        inspectedVoting.setId((long) 1);
        inspectedVoting.setVotingKey(rightVotingKey);
        inspectedVoting.setVotingTitle("title");

        firstVoteOption.setId((long) 2);
        firstVoteOption.setVoteDiscription("Discription");
        firstVoteOption.setVoting(inspectedVoting);

        secVoteOption.setId((long) 3);
        secVoteOption.setVoteDiscription("Discription");
        secVoteOption.setVoting(inspectedVoting);

        inspectedVoting.setVotingOptions(new ArrayList<VoteOption>() {
            {
               add(firstVoteOption);
               add(secVoteOption);
            }
        });
    }

    @Test
    void acceptVote() throws WrongVotingKeyException {
        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        //twice
        voteOptionService.acceptVote(firstVoteOption.getId(), votedIp, rightVotingKey);
        voteOptionService.acceptVote(firstVoteOption.getId(), votedIp, rightVotingKey);

        Mockito.verify(voteOptionRepo, times(2)).save(firstVoteOption);
        assertTrue(inspectedVoting.getTotalVotes() == 1, "total votes is not incremented correctly!");
        assertTrue(inspectedVoting.getVotedIps().contains(votedIp), "voted user's IP was not added to base!");
        assertTrue(firstVoteOption.getPluses() == 1, "total pluses is not incremented correctly!");
    }

    @Test
    void acceptVoteWrongKey() {
        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId()))
                .thenReturn(Optional.of(firstVoteOption));
        assertThrows(WrongVotingKeyException.class, () -> {
            voteOptionService.acceptVote(
                    firstVoteOption.getId(), votedIp, wrongVotingKey
            );
        }, "voting request with wrong key was not denied!");
    }
}