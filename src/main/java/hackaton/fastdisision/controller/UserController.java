package hackaton.fastdisision.controller;

import com.fasterxml.jackson.annotation.JsonView;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.NotFoundException;
import hackaton.fastdisision.service.intrface.AdminService;
import hackaton.fastdisision.service.intrface.UserService;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles requests for user state
 *
 * @author Dmitrochenkov Daniil
 * @version 1.3
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public UserController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/getAdmin")
    @JsonView(VotingView.CoreData.class)
    public User requestAdminRole(@RequestBody String clientAdminPassword,
                                 @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        return adminService.getAdminRole(currentUser, clientAdminPassword);
    }

    @GetMapping("/get/{id}")
    @JsonView(VotingView.CoreData.class)
    public User getUserData(@PathVariable("id") String id) throws NotFoundException {
        return userService.findById(id);
    }

}
