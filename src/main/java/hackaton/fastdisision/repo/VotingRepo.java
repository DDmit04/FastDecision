package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VotingRepo extends CrudRepository<Voting, Long> {

    List<Voting> findTop10ByIsPrivateVotingOrderByTotalVotesDesc(boolean isPrivate);

    List<Voting> findTop10ByIsPrivateVotingOrderByCreationDateDesc(boolean isPrivate);

    List<Voting> findByOwner_IdAndIsPrivateVotingOrderByCreationDateDesc(String id, boolean isPrivate);

}
