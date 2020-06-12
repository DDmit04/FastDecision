package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VotingDTO;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.service.intrface.VotingService;
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
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@RestController
@RequestMapping("/voteApi/charts")
public class VotingCartsController {

    private final VotingService votingService;

    @Autowired
    public VotingCartsController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("search")
    @JsonView(VotingView.MinimalData.class)
    public Page<VotingDTO> searchVotings(@RequestParam("search") String search,
                                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VotingDTO> votings = votingService.searchVotings(search, pageable);
        return votings;
    }

    @GetMapping("/newest")
    @JsonView(VotingView.ChartData.class)
    public Page<VotingDTO> getNewestVotings(@PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VotingDTO> votings = votingService.getNewest(pageable);
        return votings;
    }

    @GetMapping("/popular")
    @JsonView(VotingView.ChartData.class)
    public Page<VotingDTO> getPopularVotings(@PageableDefault Pageable pageable) {
        Page<VotingDTO> votings = votingService.getPopular(pageable);
        return votings;
    }

    @GetMapping("/userPublic/{id}")
    @JsonView(VotingView.MinimalData.class)
    public Page<VotingDTO> getUserPublicVotings(@PathVariable("id") User user,
                                             @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VotingDTO> votings = votingService.getUserPublic(user, pageable);
        return votings;
    }

    @GetMapping("/userPrivate/{id}")
    @JsonView(VotingView.MinimalData.class)
    public Page<VotingDTO> getUserPrivateVotings(@PathVariable("id") User user,
                                              @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        Page<VotingDTO> votings = votingService.getUserPrivate(user, currentUser, pageable);
        return votings;
    }

}
