package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.VoteOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repo to control votings options entities
 *
 * @author Dmitrochenkov Daniil
 * @version 1.3
 */
public interface VoteOptionRepo extends CrudRepository<VoteOption, Long> {

    /**
     * returns vote option by ID and applies fetchType.EAGER to field 'voting' in voting option
     *
     * @param id vote option ID
     * @return vote option
     * @see VoteOption
     */
    @EntityGraph(value = "votingOptionWithParent")
    Optional<VoteOption> findById(Long id);

}
