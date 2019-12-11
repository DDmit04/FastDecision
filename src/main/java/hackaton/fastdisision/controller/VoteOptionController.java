package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.service.VoteOptionService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class VoteOptionController {

    private VoteOptionService voteOptionService;

    @Autowired
    public VoteOptionController(VoteOptionService voteOptionService) {
        this.voteOptionService = voteOptionService;
    }

    @MessageMapping("/voting-websocket/{votingId}")
    @SendTo("/topic/voting/{votingId}")
    @JsonView(VotingView.CoreData.class)
    public VoteOption doVote(@DestinationVariable Long votingId, Long optionId, SimpMessageHeaderAccessor ipHandshakeInterceptor) {
        String votedIp = (String) ipHandshakeInterceptor.getSessionAttributes().get("ip");
        VoteOption voteOption = voteOptionService.acceptVote(optionId, votedIp);;
        return voteOption;
    }
}
