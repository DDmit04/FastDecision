package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VotingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repo to control votings entities
 *
 * @author Dmitrochenkov Daniil
 * @version 1.3
 * @see Voting
 * @see VotingDTO
 */
public interface VotingRepo extends CrudRepository<Voting, Long> {

    /**
     * returns voting entity by id
     *
     * @param id ID to search voting
     * @return voting
     * @see Voting
     */
    @EntityGraph(value = "coreVotingData")
    Optional<Voting> findById(Long id);

    /**
     * returns page of votings DTO ordered by total votes
     *
     * @param isPrivate get private or public votings
     * @param pageable  default pageable
     * @return page of votings DTO
     * @see VotingDTO
     */
    @Query("select new hackaton.fastdisision.data.VotingDTO(voting, sum(votingOptions.pluses)) " +
            "from Voting as voting join voting.votingOptions as votingOptions " +
            "where voting.isPrivateVoting = :isPrivate " +
            "group by voting " +
            "order by sum(votingOptions.pluses) desc")
    Page<VotingDTO> findByIsPrivateAndVoteOptionsPlusesSum(@Param("isPrivate") boolean isPrivate, Pageable pageable);

    /**
     * returns page of votings DTO ordered by total votes
     *
     * @param isPrivate get private or public votings
     * @param pageable  default pageable
     * @return page of votings DTO
     * @see VotingDTO
     */
    @EntityGraph(value = "votingGraphWithOwner")
    Page<VotingDTO> findByIsPrivateVotingOrderByCreationDate(boolean isPrivate, Pageable pageable);

    /**
     * returns page of user votings DTO ordered by creation date
     *
     * @param id        votings owner ID
     * @param isPrivate get private or public votings
     * @param pageable  default pageable
     * @return page of votings DTO
     * @see VotingDTO
     */
    @EntityGraph(value = "votingGraphWithOwner")
    Page<VotingDTO> findByOwner_IdAndIsPrivateVotingOrderByCreationDate(String id, boolean isPrivate, Pageable pageable);

    /**
     * returns page of votings DTO which titles contains searched string
     *
     * @param isPrivate get private or public votings
     * @param title     string to search
     * @param pageable  default pageable
     * @return page of votings DTO
     * @see VotingDTO
     */
    @EntityGraph(value = "votingGraphWithOwner")
    Page<VotingDTO> findAllByIsPrivateVotingAndVotingTitleContains(boolean isPrivate, String title, Pageable pageable);

}
