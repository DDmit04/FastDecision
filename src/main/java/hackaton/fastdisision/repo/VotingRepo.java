package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Repo to control votings entities
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
public interface VotingRepo extends CrudRepository<Voting, Long> {

    /**
     * returns page of votings ordered by total votes
     * @param isPrivate get private or public votings
     * @param pageable default pageable
     * @return page of votings
     * @see Voting
     */
    Page<Voting> findByIsPrivateVotingOrderByTotalVotes(boolean isPrivate, Pageable pageable);

    /**
     * returns page of votings ordered by total votes
     * @param isPrivate get private or public votings
     * @param pageable default pageable
     * @return page of votings
     * @see Voting
     */
    Page<Voting> findByIsPrivateVotingOrderByCreationDate(boolean isPrivate, Pageable pageable);

    /**
     * returns page of user votings ordered by creation date
     * @param id votings owner ID
     * @param isPrivate get private or public votings
     * @param pageable default pageable
     * @return page of votings
     * @see Voting
     */
    Page<Voting> findByOwner_IdAndIsPrivateVotingOrderByCreationDate(String id, boolean isPrivate, Pageable pageable);

    /**
     * returns page of votings which titles contains searched string
     * @param isPrivate get private or public votings
     * @param title string to search
     * @param pageable default pageable
     * @return page of votings
     * @see Voting
     */
    Page<Voting> findAllByIsPrivateVotingAndVotingTitleContains(boolean isPrivate, String title, Pageable pageable);

}
