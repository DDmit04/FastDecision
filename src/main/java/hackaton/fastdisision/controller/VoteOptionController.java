package hackaton.fastdisision.controller;

import hackaton.fastdisision.config.IpHandshakeInterceptor;
import hackaton.fastdisision.data.Vote;
import hackaton.fastdisision.data.VoteOption;
import hackaton.fastdisision.repo.OptionRepo;
import hackaton.fastdisision.repo.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

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
    public VoteOption doVote(@DestinationVariable Long votingId, Long optionId, SimpMessageHeaderAccessor ipHandshakeInterceptor) {
        VoteOption voteOption = optionRepo.findVoteOptionById(optionId);
        Vote vote = voteRepo.findVotingById(votingId);
        String ip = (String) ipHandshakeInterceptor.getSessionAttributes().get("ip");
        int ipIndex = 0;
        for(VoteOption voteOpt : vote.getVoteOptions()) {
            ipIndex = voteOpt.getVotedIps().indexOf(ip);
            if(ipIndex != -1) {
                break;
            }
        }
        if(ipIndex == -1) {
            voteOption.getVotedIps().add(ip);
            voteOption.setPluses(voteOption.getPluses() + 1);
        }
        return optionRepo.save(voteOption);
    }
}
