package hackaton.fastdisision.service.intrface;

import hackaton.fastdisision.data.VoteOption;

/**
 * Service to manipulate voting options entities
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
public interface VoteOptionService {

    /**
     * Add new vote to vote option
     *
     * @param voteOption vote option to add vote
     * @return vote option by optionId (with accepted vote or not)
     * @see VoteOption
     */
    VoteOption acceptVote(VoteOption voteOption);
}
