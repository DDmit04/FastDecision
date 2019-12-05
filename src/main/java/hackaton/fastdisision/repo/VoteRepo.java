package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Vote;
import hackaton.fastdisision.data.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepo extends JpaRepository<Vote, Long> {

    @Query("from Vote vo where vo.voteTitle = :title group by vo")
    Vote findByTitle( String title);

    @Query("from Vote vo where vo.id = :id group by vo")
    Vote findVotingById(Long id);

}
