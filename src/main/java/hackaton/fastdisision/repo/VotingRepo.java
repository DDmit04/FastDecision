package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VotingRepo extends JpaRepository<Voting, Long> {

    List<Voting> findTop10ByTotalVotes(int totalVotes);

}
