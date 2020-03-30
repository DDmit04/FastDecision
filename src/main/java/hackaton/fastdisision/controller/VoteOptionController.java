package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.excaptions.WrongVotingKeyException;
import hackaton.fastdisision.service.VoteOptionService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Controller that handles requests for websocket votes
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@Controller
public class VoteOptionController {

    private VoteOptionService voteOptionService;

    @Autowired
    public VoteOptionController(VoteOptionService voteOptionService) {
        this.voteOptionService = voteOptionService;
    }

    @MessageMapping("/voting-websocket/{votingId}/{votingKey}")
    @SendTo("/topic/voting/{votingId}/{votingKey}")
    @JsonView(VotingView.MinimalData.class)
    public VoteOption doVote(@DestinationVariable Long votingId,
                             @DestinationVariable String votingKey,
                             Long optionId,
                             SimpMessageHeaderAccessor ipHandshakeInterceptor) throws WrongVotingKeyException {
        String votedIp = (String) ipHandshakeInterceptor.getSessionAttributes().get("ip");
        VoteOption voteOption = voteOptionService.acceptVote(optionId, votedIp, votingKey);
        return voteOption;
    }
}
