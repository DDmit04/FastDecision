package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.OptionRepo;
import hackaton.fastdisision.repo.VoteRepo;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class VoteOptionController {

    private OptionRepo optionRepo;
    private VoteRepo voteRepo;

    @Autowired
    public VoteOptionController(OptionRepo optionRepo, VoteRepo voteRepo) {
        this.optionRepo = optionRepo;
        this.voteRepo = voteRepo;
    }

    @MessageMapping("/voting-websocket/{votingId}")
    @SendTo("/topic/voting/{votingId}")
    @JsonView(VotingView.CoreData.class)
    public VoteOption doVote(@DestinationVariable Long votingId, Long optionId, SimpMessageHeaderAccessor ipHandshakeInterceptor) {
        VoteOption voteOption = optionRepo.findVoteOptionById(optionId);
        Voting voting = voteRepo.findVotingById(votingId);
        String ip = (String) ipHandshakeInterceptor.getSessionAttributes().get("ip");
        int ipIndex = 0;
        for(VoteOption voteOpt : voting.getVoteOptions()) {
            ipIndex = voteOpt.getVotedIps().indexOf(ip);
            if(ipIndex != -1) {
                break;
            }
        }
        if(ipIndex == -1) {
            voteOption.getVotedIps().add(ip);
            voteOption.setPluses(voteOption.getPluses() + 1);
            voting.setTotalVotes(voting.getTotalVotes() + 1);
        }
        return optionRepo.save(voteOption);
    }
}
