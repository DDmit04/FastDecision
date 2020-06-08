package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.WrongVotingKeyException;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    public void initData() {
        inspectedVoting = new Voting();
        firstVoteOption = new VoteOption();
        secVoteOption = new VoteOption();
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
    void acceptVoteCheckingIp() throws WrongVotingKeyException {

        inspectedVoting.setCheckingIpVoting(true);

        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        //twice
        voteOptionService.acceptVote(firstVoteOption.getId(), votedIp, rightVotingKey);
        voteOptionService.acceptVote(firstVoteOption.getId(), votedIp, rightVotingKey);

        Mockito.verify(voteOptionRepo, times(1)).save(firstVoteOption);
        Mockito.verify(votingRepo, times(2)).save(inspectedVoting);
        assertEquals(1, inspectedVoting.getTotalVotes(), "total votes is not incremented correctly!");
        assertTrue(inspectedVoting.getVotedIps().contains(votedIp), "voted user's IP was not added to base!");
        assertEquals(1, firstVoteOption.getPluses(), "total pluses is not incremented correctly!");
    }

    @Test
    void acceptVoteUnCheckingIp() throws WrongVotingKeyException {

        inspectedVoting.setCheckingIpVoting(false);

        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        //twice
        voteOptionService.acceptVote(firstVoteOption.getId(), votedIp, rightVotingKey);
        voteOptionService.acceptVote(firstVoteOption.getId(), votedIp, rightVotingKey);

        Mockito.verify(voteOptionRepo, times(2)).save(firstVoteOption);
        Mockito.verify(votingRepo, times(2)).save(inspectedVoting);
        assertEquals(2, inspectedVoting.getTotalVotes(), "total votes is not incremented correctly!");
        assertFalse(inspectedVoting.getVotedIps().contains(votedIp), "voted user's IP was added to base!");
        assertEquals(2, firstVoteOption.getPluses(), "total pluses is not incremented correctly!");
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