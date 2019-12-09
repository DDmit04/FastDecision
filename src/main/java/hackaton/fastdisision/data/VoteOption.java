package hackaton.fastdisision.data;


import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.views.VotingView;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(VotingView.Id.class)
    private long id;

    @JsonView(VotingView.CoreData.class)
    private String voteDiscription;
    @JsonView(VotingView.MinimalData.class)
    private long pluses;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="votedIps1", joinColumns=@JoinColumn(name="vote_option_id"))
    @JsonView(VotingView.CoreData.class)
    private List<String> votedIps = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="vote_id")
    @JsonView(VotingView.FullData.class)
    private Voting voting;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteOption that = (VoteOption) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
