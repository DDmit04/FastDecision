package hackaton.fastdisision.repo;

import hackaton.fastdisision.data.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepo extends JpaRepository<Voting, Long> {

    @Query("from Voting vo where vo.voteTitle = :title group by vo")
    Voting findByTitle(String title);

    @Query("from Voting vo where vo.id = :id group by vo")
    Voting findVotingById(Long id);

}
