package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(VotingView.Id.class)
    private long id;

    @JsonView(VotingView.MinimalData.class)
    private int totalVotes;

    @JsonView(VotingView.MinimalData.class)
    private String voteTitle;

    @JsonView(VotingView.FullData.class)
    private LocalDateTime creationDate;

    @JsonView(VotingView.FullData.class)
    private boolean isPrivateVoting;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="voted_users_ips", joinColumns=@JoinColumn(name="vote_option_id"))
    @JsonView(VotingView.FullData.class)
    private List<String> votedIps = new ArrayList<>();

    @OneToMany(mappedBy="voting", cascade=CascadeType.ALL)
    @JsonView(VotingView.CoreData.class)
    private List<VoteOption> voteOptions;

}
