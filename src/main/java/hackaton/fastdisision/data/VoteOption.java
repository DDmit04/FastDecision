package hackaton.fastdisision.data;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(VotingView.Id.class)
    private long id;

    @JsonView(VotingView.MinimalData.class)
    private String voteDiscription;
    @JsonView(VotingView.MinimalData.class)
    private long pluses;

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
