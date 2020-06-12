package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.VotingNotFoundException;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.repo.VotingRepo;
import hackaton.fastdisision.service.intrface.VotingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 */
class VotingServiceTest extends BasicTest {

    @Autowired
    private VotingService votingService;

    @MockBean
    private VotingRepo votingRepo;

    @MockBean
    private UserRepo userRepo;

    private static User commonUser;
    private static User adminUser;
    private static Voting voting;

    private static VoteOption firstVoteOption;
    private static VoteOption secVoteOption;

    private static String rightVotingKey = "rightKey";
    private static String voteIp = "0.0.0.0";


    @BeforeAll
    public static void initData() {

        commonUser = new User();
        commonUser.setId("1");
        commonUser.setUsername("1");
        commonUser.setPassword("1");
        commonUser.setRoles(new HashSet<UserRole>() {
            {
                add(UserRole.USER);
            }
        });

        adminUser = new User();
        adminUser.setId("2");
        adminUser.setUsername("2");
        adminUser.setPassword("2");
        adminUser.setRoles(new HashSet<UserRole>() {
            {
                add(UserRole.USER);
                add(UserRole.ADMIN);
            }
        });

        voting = new Voting();
        voting.setId((long) 1);
        voting.setVotingKey(rightVotingKey);
        voting.setVotingTitle("title");

        firstVoteOption = new VoteOption();
        firstVoteOption.setId((long) 2);
        firstVoteOption.setVoteDiscription("Discription");

        secVoteOption = new VoteOption();
        secVoteOption.setId((long) 3);
        secVoteOption.setVoteDiscription("Discription");

        voting.setVotingOptions(new ArrayList<>(Arrays.asList(firstVoteOption, secVoteOption)));
    }

    @Test
    void addVotingWithOwner() {
        Mockito.when(votingRepo.save(voting)).thenReturn(voting);

        votingService.addVoting(voting, commonUser);

        voting.getVotingOptions().stream()
                .peek(voteOption -> assertTrue(voteOption.getVoting().equals(voting)));
        Mockito.verify(votingRepo, times(1)).save(voting);
        assertNotNull(voting.getOwner(), "voting owner is empty!");
        assertNotNull(voting.getVotingKey(), "voting key is empty!");
        assertNotNull(voting.getCreationDate(), "voting creation date is empty!");
    }

    @Test
    void deleteVotingByCommonUser() throws AccessDeniedException {
        commonUser.getUserVotings().add(voting);
        voting.setOwner(commonUser);
        votingService.deleteVoting(voting, commonUser);

        Mockito.verify(userRepo, times(1)).save(commonUser);
        Mockito.verify(votingRepo, times(1)).delete(voting);
        assertFalse(commonUser.getUserVotings().contains(voting), "deleted voting still present in user's votings!");
    }

    @Test
    void deleteVotingByCommonUserFailed() {
        voting.setOwner(adminUser);
        adminUser.getUserVotings().add(voting);
        assertThrows(AccessDeniedException.class, () -> {
            votingService.deleteVoting(voting, commonUser);
        }, "voting must not be deleted and throw exception!");
    }

    @Test
    void deleteVotingByAdminUser() throws AccessDeniedException {
        commonUser.getUserVotings().add(voting);
        voting.setOwner(commonUser);
        votingService.deleteVoting(voting, adminUser);

        Mockito.verify(userRepo, times(1)).save(commonUser);
        Mockito.verify(votingRepo, times(1)).delete(voting);
        assertFalse(commonUser.getUserVotings().contains(voting), "deleted by admin voting still present in user's votings!");
    }

    @Test
    void validateVotingKey() throws VotingNotFoundException {
        boolean votingKeyIsValid = votingService.validateVotingKey(voting, rightVotingKey);
        assertTrue(votingKeyIsValid, "Key must be valid!");
    }

    @Test
    void validateVotingKeyNullVoting() {
        assertThrows(VotingNotFoundException.class, () -> {
            votingService.validateVotingKey(null, rightVotingKey);
        }, "Null voting must be detected!");
    }

    @Test
    void addVotedIp() {
        votingService.addVotedIp(voting, voteIp);
        Mockito.verify(votingRepo, times(1)).save(voting);
        assertTrue(voting.getVotedIps().contains(voteIp));
    }

}