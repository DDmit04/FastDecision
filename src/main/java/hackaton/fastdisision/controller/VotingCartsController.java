package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.service.VotingService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles requests for voting charts/pages
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
@RestController
@RequestMapping("/voteApi/charts")
public class VotingCartsController {

    private VotingService votingService;

    @Autowired
    public VotingCartsController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("search")
    @JsonView(VotingView.MinimalData.class)
    public Page<Voting> searchVotings(@RequestParam("search") String search,
                                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Voting> votings = votingService.searchVotings(search, pageable);
        return votings;
    }

    @GetMapping("/newest")
    @JsonView(VotingView.MinimalData.class)
    public Page<Voting> getNewestVotings(@PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Voting> votings = votingService.getNewest(pageable);
        return votings;
    }

    @GetMapping("/popular")
    @JsonView(VotingView.MinimalData.class)
    public Page<Voting> getPopularVotings(@PageableDefault(sort = {"totalVotes"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Voting> votings = votingService.getPopular(pageable);
        return votings;
    }

    @GetMapping("/userPublic/{id}")
    @JsonView(VotingView.MinimalData.class)
    public Page<Voting> getUserPublicVotings(@PathVariable("id") User user,
                                             @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Voting> votings = votingService.getUserPublic(user, pageable);
        return votings;
    }

    @GetMapping("/userPrivate/{id}")
    @JsonView(VotingView.MinimalData.class)
    public Page<Voting> getUserPrivateVotings(@PathVariable("id") User user,
                                              @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        Page<Voting> votings = votingService.getUserPrivate(user, currentUser, pageable);
        return votings;
    }

}
