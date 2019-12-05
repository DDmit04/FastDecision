package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int totalVotes;
    private String voteTitle;


    @OneToMany(mappedBy="vote", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VoteOption> voteOptions;

}
