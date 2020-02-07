package hackaton.fastdisision.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hackaton.fastdisision.views.VotingView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "usr")
@Entity
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class User {

    @Id
    @JsonView(VotingView.Id.class)
    private String id;

    @JsonView(VotingView.MinimalData.class)
    private String username;

    @JsonView(VotingView.FullData.class)
    private String email;

    @JsonView(VotingView.FullData.class)
    private String password;

    @JsonView(VotingView.MinimalData.class)
    private String userPic;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JsonView(VotingView.FullData.class)
    private Set<Voting> userVotings = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getId() != null ? getId().equals(user.getId()) : user.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

}