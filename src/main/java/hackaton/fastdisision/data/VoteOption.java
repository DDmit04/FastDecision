package hackaton.fastdisision.data;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Represents vote option entity
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
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
    private Long id;

    @JsonView(VotingView.MinimalData.class)
    @NotBlank(message = "vote discription can not be empty")
    private String voteDiscription;

    @JsonView(VotingView.MinimalData.class)
    private long pluses = 0;

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
