package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.service.intrface.AdminService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles requests for admin role
 *
 * @author Dmitrochenkov Daniil
 * @version 1.2
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/giveAdmin/{id}")
    @JsonView(VotingView.CoreData.class)
    public User giveAdminRole(@PathVariable("id") User user,
                              @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        return adminService.giveAdminRole(user, currentUser);
    }

    @PostMapping("/removeAdmin/{id}")
    @JsonView(VotingView.CoreData.class)
    public User removeAdmin(@PathVariable("id") User user,
                            @RequestBody(required = false) String clientAdminPassword,
                            @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        if (clientAdminPassword == null) {
            clientAdminPassword = "";
        }
        return adminService.removeAdminRole(user, currentUser, clientAdminPassword);
    }

}
