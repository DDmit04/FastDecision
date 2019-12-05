package hackaton.fastdisision.data;


import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="vote_id")
    private Vote vote;

    private String voteDiscription;
    private long pluses;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="votedIps1", joinColumns=@JoinColumn(name="vote_option_id"))
    private List<String> votedIps = new ArrayList<>();

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
