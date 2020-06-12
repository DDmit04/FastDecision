package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.service.intrface.AdminService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles requests for user state
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final AdminService adminService;

    @Autowired
    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/getAdmin")
    @JsonView(VotingView.CoreData.class)
    public User requestAdminRole(@RequestBody String clientAdminPassword,
                                 @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        User user = adminService.getAdminRole(currentUser, clientAdminPassword);
        return user;
    }

}
