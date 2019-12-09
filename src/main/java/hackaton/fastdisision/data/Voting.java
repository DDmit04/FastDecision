package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Data;

import javax.persistence.*;
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
    @JsonView(VotingView.CoreData.class)
    private String voteTitle;


    @OneToMany(mappedBy="voting", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(VotingView.CoreData.class)
    private List<VoteOption> voteOptions;

}
