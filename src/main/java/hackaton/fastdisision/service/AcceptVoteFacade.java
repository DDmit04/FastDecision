package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.excaptions.VoteException;
import hackaton.fastdisision.excaptions.VotingAccessException;

/**
 * Service to accept vote in voting and votingOption
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
public interface AcceptVoteFacade {

    /**
     * Accept new vote to voting
     *
     * @param user voted user
     * @param optionId voted option ID in voting
     * @param votedIp IP which sent vote
     * @param votingKey voting key from client
     * @return updated voting option
     * @throws VotingAccessException if voting key is invalid
     * @throws VoteException if voting accept only one vote per IP and votedIp already vote
     * @see VoteOption
     */
    VoteOption acceptVote(User user, Long optionId, String votedIp, String votingKey) throws VotingAccessException, VoteException;
}
