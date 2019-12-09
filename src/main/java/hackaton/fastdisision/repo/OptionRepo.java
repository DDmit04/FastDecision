package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OptionRepo extends JpaRepository<VoteOption, Long> {

    @Query("from VoteOption vo where vo.id = :id group by vo")
    VoteOption findVoteOptionById(Long id);

}
