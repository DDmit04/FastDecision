package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.VoteOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteOptionRepo extends JpaRepository<VoteOption, Long> {

    @EntityGraph(attributePaths = { "voting" })
    @Query("from VoteOption vo where vo.id = :id")
    VoteOption findVoteOptionById(Long id);

}
