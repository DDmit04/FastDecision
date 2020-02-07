package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VotingRepo extends CrudRepository<Voting, Long> {

    List<Voting> findTop10ByIsPrivateVotingOrderByTotalVotesDesc(boolean isPrivate);

    List<Voting> findTop10ByIsPrivateVotingOrderByCreationDateDesc(boolean isPrivate);

    List<Voting> findTop10ByOwner_IdAndIsPrivateVotingOrderByCreationDateDesc(String id, boolean isPrivate);

}
