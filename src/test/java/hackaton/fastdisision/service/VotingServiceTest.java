package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class VotingServiceTest {

    @Autowired
    private VotingService votingService;

    @MockBean
    private VotingRepo votingRepo;

    @MockBean
    private UserRepo userRepo;

    private static User commonUser;
    private static User adminUser;
    private static Voting voting;

    @BeforeAll
    public static void initData() {

        commonUser = new User();
        commonUser.setId("1");
        commonUser.setUsername("1");
        commonUser.setPassword("1");
        commonUser.setRoles(new HashSet<>(Collections.singleton(UserRoles.USER)));

        adminUser = new User();
        adminUser.setId("2");
        adminUser.setUsername("2");
        adminUser.setPassword("2");
        adminUser.setRoles(new HashSet<>(Collections.singleton(UserRoles.ADMIN)));

        voting = new Voting();
        voting.setId(1);
        voting.setVotingTitle("title");

        VoteOption firstVoteOption = new VoteOption();
        firstVoteOption.setId(firstVoteOption.getId());
        firstVoteOption.setVoteDiscription("Discription");

        VoteOption secVoteOption = new VoteOption();
        secVoteOption.setId(secVoteOption.getId());
        secVoteOption.setVoteDiscription("Discription");

        voting.setVotingOptions(new ArrayList<VoteOption>() {
            {
                add(firstVoteOption);
                add(secVoteOption);
            }
        });
    }

    @Test
    void addVotingWithOwner() {
        votingService.addVoting(voting, commonUser);
        for(VoteOption voteOption: voting.getVotingOptions()) {
            assertTrue(voteOption.getVoting().equals(voting));
        }
        assertNotNull(voting.getOwner(), "voting owner is empty!");
        assertNotNull(voting.getVotingKey(), "voting key is empty!");
        assertNotNull(voting.getCreationDate(), "voting creation date is empty!");
    }

    @Test
    void deleteVotingByCommonUser() throws AccessDeniedException {
        commonUser.getUserVotings().add(voting);
        voting.setOwner(commonUser);
        votingService.deleteVoting(voting, commonUser);
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
        assertFalse(commonUser.getUserVotings().contains(voting), "deleted by admin voting still present in user's votings!");
    }

}