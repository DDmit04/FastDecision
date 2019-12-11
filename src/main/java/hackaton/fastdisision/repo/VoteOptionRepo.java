package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.data.Voting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VoteOptionRepo extends CrudRepository<VoteOption, Long> {

    @EntityGraph(attributePaths = { "voting" })
    Optional<VoteOption> findById(Long id);

}
