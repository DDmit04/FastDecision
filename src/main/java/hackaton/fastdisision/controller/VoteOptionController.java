package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.excaptions.VoteException;
import hackaton.fastdisision.excaptions.VotingAccessException;
import hackaton.fastdisision.service.AcceptVoteFacade;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Controller that handles requests for websocket votes
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@Controller
public class VoteOptionController {

    private final AcceptVoteFacade acceptVoteFacade;

    @Autowired
    public VoteOptionController(AcceptVoteFacade acceptVoteFacade) {
        this.acceptVoteFacade = acceptVoteFacade;
    }

    @MessageMapping("/voting-websocket/{votingId}/{votingKey}")
    @SendTo("/topic/voting/{votingId}/{votingKey}")
    @JsonView(VotingView.MinimalData.class)
    public VoteOption doVote(@DestinationVariable Long votingId,
                             @DestinationVariable String votingKey,
                             @Payload Long optionId,
                             SimpMessageHeaderAccessor ipHandshakeInterceptor,
                             Principal principal) throws VotingAccessException, VoteException {
        String votedIp = (String) ipHandshakeInterceptor.getSessionAttributes().get("ip");
        User user = (User) ((Authentication) principal).getPrincipal();
        VoteOption voteOption = acceptVoteFacade.acceptVote(user, optionId, votedIp, votingKey);
        return voteOption;
    }
}
