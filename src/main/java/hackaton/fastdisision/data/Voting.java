package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import hackaton.fastdisision.excaptions.VotingAccessException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents voting entity
 *
 * @author Dmitrochenkov Daniil
 * @version 1.1
 */
@Getter
@Setter
@Entity
@Table(indexes = {
        @Index(columnList = "votingTitle", name = "voting_title_index"),
        @Index(columnList = "creationDate", name = "voting_creation_date_index"),
})
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private Long id;

    @NotBlank(message = "voting title can not be empty")
    private String votingTitle;

    private LocalDateTime creationDate;

    private String votingKey;

    //@JsonProperty because Lombok generates wrong setters for JSON serializer (if name of bool field starts with 'is')
    @JsonProperty
    private boolean isProtectedVoting;

    @JsonProperty
    private boolean isPrivateVoting;

    @JsonProperty
    private boolean isCheckingIpVoting;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "voted_users_ips", joinColumns = @JoinColumn(name = "vote_option_id"))
    private List<String> votedIps = new ArrayList<>();

    @OneToMany(mappedBy = "voting", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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

    public boolean userTryAccessVoting(User user, String key) {
        boolean userIsPresent = user != null;
        boolean userIsAdmin = userIsPresent && user.isAdmin();

        if (userIsAdmin || isOwner(user)) {
            return true;
        } else if (isProtectedVoting && !keyMatches(key)) {
            throw new VotingAccessException("Voting key is invalid!");
        } else if (isPrivateVoting) {
            throw new VotingAccessException(HttpStatus.NOT_ACCEPTABLE, "You can not access this voting!");
        } else {
            return true;
        }
    }

    public boolean isOwner(User user) {
        return owner != null && user != null && owner.equals(user);
    }

    public boolean keyMatches(String key) {
        return votingKey.equals(key);
    }

    public boolean isIpAlreadyVote(String ip) {
        return votedIps.contains(ip);
    }

    public void updateVotingOptionToVotingRelationship(Voting voting) {
        votingOptions = votingOptions.stream()
                .peek(voteOption -> voteOption.setVoting(voting))
                .collect(Collectors.toList());
    }
}
