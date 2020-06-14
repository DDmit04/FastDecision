package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.views.VotingView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Voting class
 *
 * @author Daniil Dmitrochenkov
 * @vrsion 1.2
 * @see Voting
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VotingDTO {

    public VotingDTO(Voting voting) {
        this.id = voting.getId();
        this.votingTitle = voting.getVotingTitle();
        this.creationDate = voting.getCreationDate();
        this.votingKey = voting.getVotingKey();
        this.isProtectedVoting = voting.isProtectedVoting();
        this.isPrivateVoting = voting.isPrivateVoting();
        this.isCheckingIpVoting = voting.isCheckingIpVoting();
        this.owner = voting.getOwner();
        this.votedIps = voting.getVotedIps();
        this.votingOptions = voting.getVotingOptions();
        this.totalVotes = voting.getVotingOptions().stream()
                .mapToLong(VoteOption::getPluses)
                .reduce(Long::sum).orElse(0);
    }

    public VotingDTO(Voting voting, long totalVotes) {
        this(
                voting.getId(),
                totalVotes,
                voting.getVotingTitle(),
                voting.isProtectedVoting(),
                voting.getOwner(),
                voting.getVotingOptions(),
                voting.getVotingKey(),
                voting.getCreationDate(),
                voting.isPrivateVoting(),
                voting.isCheckingIpVoting(),
                voting.getVotedIps()
        );
    }

    @JsonView(VotingView.Id.class)
    private Long id;

    @JsonView(VotingView.ChartData.class)
    private long totalVotes = 0;

    @JsonView(VotingView.ChartData.class)
    private String votingTitle;

    @JsonView(VotingView.ChartData.class)
    private boolean isProtectedVoting = false;

    @JsonView(VotingView.ChartData.class)
    private User owner;

    @JsonView(VotingView.MinimalData.class)
    private List<VoteOption> votingOptions;

    @JsonView(VotingView.CoreData.class)
    private String votingKey;

    @JsonView(VotingView.FullData.class)
    private LocalDateTime creationDate;

    @JsonView(VotingView.FullData.class)
    private boolean isPrivateVoting = false;

    @JsonView(VotingView.FullData.class)
    private boolean isCheckingIpVoting = false;

    @JsonView(VotingView.FullData.class)
    private List<String> votedIps = new ArrayList<>();



}
