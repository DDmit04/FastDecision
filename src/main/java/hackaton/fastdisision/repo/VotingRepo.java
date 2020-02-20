package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface VotingRepo extends CrudRepository<Voting, Long> {

    Page<Voting> findByIsPrivateVotingOrderByTotalVotes(boolean isPrivate, Pageable pageable);

    Page<Voting> findByIsPrivateVotingOrderByCreationDate(boolean isPrivate, Pageable pageable);

    Page<Voting> findByOwner_IdAndIsPrivateVotingOrderByCreationDate(String id, boolean isPrivate, Pageable pageable);

    Page<Voting> findAllByIsPrivateVotingAndVotingTitleContains(boolean isPrivate, String title, Pageable pageable);

}
