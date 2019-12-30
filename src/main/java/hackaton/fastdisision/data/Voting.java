package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(VotingView.Id.class)
    private long id;

    @JsonView(VotingView.MinimalData.class)
    private int totalVotes;

    @JsonView(VotingView.MinimalData.class)
    private String votingTitle;

    @JsonView(VotingView.FullData.class)
    private LocalDateTime creationDate;

    @JsonView(VotingView.FullData.class)
    private boolean isPrivateVoting;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="owner_id")
    @JsonView(VotingView.MinimalData.class)
    private User owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="voted_users_ips", joinColumns=@JoinColumn(name="vote_option_id"))
    @JsonView(VotingView.FullData.class)
    private List<String> votedIps = new ArrayList<>();

    @OneToMany(mappedBy="voting", cascade=CascadeType.ALL, orphanRemoval = true)
    @JsonView(VotingView.CoreData.class)
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
