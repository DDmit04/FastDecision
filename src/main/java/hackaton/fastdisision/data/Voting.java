package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.views.VotingView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents voting entity
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@Getter
@Setter
@Entity
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(VotingView.Id.class)
    private Long id;

    @JsonView(VotingView.MinimalData.class)
    private int totalVotes = 0;

    @JsonView(VotingView.MinimalData.class)
    @NotBlank(message = "voting title can not be empty")
    private String votingTitle;

    @JsonView(VotingView.FullData.class)
    private LocalDateTime creationDate;

    @JsonView(VotingView.CoreData.class)
    private String votingKey;

    @JsonView(VotingView.MinimalData.class)
    private boolean isProtectedVoting = false;

    @JsonView(VotingView.FullData.class)
    private boolean isPrivateVoting = false;

    @JsonView(VotingView.FullData.class)
    private boolean isCheckingIpVoting = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="owner_id")
    @JsonView(VotingView.MinimalData.class)
    private User owner;

    @JsonView(VotingView.FullData.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="voted_users_ips", joinColumns=@JoinColumn(name="vote_option_id"))
    private List<String> votedIps = new ArrayList<>();

    @JsonView(VotingView.MinimalData.class)
    @OneToMany(mappedBy="voting", cascade=CascadeType.ALL, orphanRemoval = true)
    @Valid
    @Size(min = 2, max = 32, message = "voting options count must be between 2 and 32!")
    @NotNull(message = "voting must contain vote options!")
    private List<VoteOption> votingOptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voting)) return false;

        Voting voting = (Voting) o;

        return getId() == voting.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
