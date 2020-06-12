package hackaton.fastdisision.service.intrface;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VotingDTO;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.excaptions.VotingNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service to manipulate votings entities
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
public interface VotingService {

    /**
     * Create voting (set creation data, voting key, owner and parent voting for options)
     *
     * @param voting voting to create
     * @param user   user requested creation voting
     * @return saved voting
     * @see Voting
     * @see User
     */
    VotingDTO addVoting(Voting voting, User user);

    /**
     * Delete voting and remove it from users's votings
     *
     * @param voting voting to delete
     * @param user   user requested deletion voting
     * @throws AccessDeniedException user isn't voting owner
     * @see Voting
     * @see User
     */
    void deleteVoting(Voting voting, User user) throws AccessDeniedException;

    /**
     * Returns page of votings sorted by total votes count
     *
     * @param pageable requested pageable
     * @return page of popular votings DTO
     * @see Voting
     */
    Page<VotingDTO> getPopular(Pageable pageable);

    /**
     * Returns page of votings sorted by creation date
     *
     * @param pageable requested pageable
     * @return page of newest votings DTO
     * @see Voting
     */
    Page<VotingDTO> getNewest(Pageable pageable);

    /**
     * @param user     user requested user public votings
     * @param pageable requested pageable
     * @return page of public user votings DTO
     * @see Voting
     */
    Page<VotingDTO> getUserPublic(User user, Pageable pageable);

    /**
     * Returns user private votings
     *
     * @param user        user to get private votings
     * @param currentUser user requested private votings
     * @param pageable    requested pageable
     * @return page of private user votings DTO
     * @throws AccessDeniedException user neither currentUser nor admin or user is null
     * @see Voting
     */
    Page<VotingDTO> getUserPrivate(User user, User currentUser, Pageable pageable) throws AccessDeniedException;

    /**
     * Returns votings which title contains string to search
     *
     * @param search   string to search
     * @param pageable requested pageable
     * @return page of votings DTO includes searched string
     * @see Voting
     */
    Page<VotingDTO> searchVotings(String search, Pageable pageable);

    /**
     * Search voting by ID and convert to DTO
     *
     * @param user
     * @param votingKey
     * @param id
     * @return
     * @throws VotingNotFoundException
     * @throws VotingAccessException
     */
    VotingDTO findVotingDtoById(User user, String votingKey, long id) throws VotingNotFoundException, VotingAccessException;

    /**
     * Check voting key
     *
     * @param voting voting to check key
     * @param votingKey key to check
     * @return is votingKey valid for voting
     * @throws VotingNotFoundException if voting = null
     * @see Voting
     */
    boolean validateVotingKey(Voting voting, String votingKey) throws VotingNotFoundException;

    /**
     * Add new IP to voting voted IPs
     *
     * @param voting voting to add IP
     * @param votedIp IP to add
     * @see Voting
     */
    void addVotedIp(Voting voting, String votedIp);
}
