package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VotingRepo extends JpaRepository<Voting, Long> {

}
