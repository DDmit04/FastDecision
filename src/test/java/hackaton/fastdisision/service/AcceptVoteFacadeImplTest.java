package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.VoteException;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.repo.VoteOptionRepo;
import hackaton.fastdisision.service.intrface.VoteOptionService;
import hackaton.fastdisision.service.intrface.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.3
 */
class AcceptVoteFacadeImplTest extends BasicTest {

    @Autowired
    private AcceptVoteFacade acceptVoteFacade;

    @MockBean
    private VotingService votingService;

    @MockBean
    private VoteOptionService voteOptionService;

    @MockBean
    private VoteOptionRepo voteOptionRepo;

    private static Voting voting;

    private static VoteOption firstVoteOption;
    private static VoteOption secVoteOption;

    private static String rightVotingKey = "rightKey";
    private static String wrongVotingKey = "wrongKey";
    private static String voteIp = "0.0.0.0";

    @BeforeEach
    public void initData() {

        voting = new Voting();
        voting.setId((long) 1);
        voting.setVotingKey(rightVotingKey);
        voting.setVotingTitle("title");
        voting.setProtectedVoting(true);

        firstVoteOption = new VoteOption();
        firstVoteOption.setId((long) 2);
        firstVoteOption.setVoteDiscription("Discription");
        firstVoteOption.setVoting(voting);

        secVoteOption = new VoteOption();
        secVoteOption.setId((long) 3);
        secVoteOption.setVoteDiscription("Discription");
        secVoteOption.setVoting(voting);

        voting.setVotingOptions(new ArrayList<>(Arrays.asList(firstVoteOption, secVoteOption)));

    }

    @Test
    void acceptVoteUnCheckingIp() throws VoteException, VotingAccessException {
        User votedUser = null;
        voting.setCheckingIpVoting(false);

        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        acceptVoteFacade.acceptVote(votedUser, firstVoteOption.getId(), voteIp, rightVotingKey);

        Mockito.verify(voteOptionService).acceptVote(firstVoteOption);
    }

    @Test
    void acceptVoteCheckingIp() throws VoteException, VotingAccessException {
        voting.setCheckingIpVoting(true);
        User votedUser = null;

        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        acceptVoteFacade.acceptVote(votedUser, firstVoteOption.getId(), voteIp, rightVotingKey);

        Mockito.verify(voteOptionService).acceptVote(firstVoteOption);
        Mockito.verify(votingService).addVotedIp(firstVoteOption.getVoting(), voteIp);
    }

    @Test
    void acceptVoteCheckingAlreadyVotedIp() {
        User votedUser = null;
        voting.setCheckingIpVoting(true);
        voting.getVotedIps().add(voteIp);
        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        assertThrows(VoteException.class, () -> acceptVoteFacade.acceptVote(votedUser, firstVoteOption.getId(), voteIp, rightVotingKey), "Voted IP can not vote again!");
    }

    @Test
    void acceptVoteWrongKey() {
        User votedUser = null;

        Mockito.when(voteOptionRepo.findById(firstVoteOption.getId())).thenReturn(Optional.of(firstVoteOption));

        assertThrows(VotingAccessException.class, () -> acceptVoteFacade.acceptVote(votedUser, firstVoteOption.getId(), voteIp, wrongVotingKey), "Invalid key must throw exception!");
    }


}