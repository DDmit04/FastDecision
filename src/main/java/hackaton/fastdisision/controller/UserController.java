package hackaton.fastdisision.controller;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.excaptions.VotingNotFoundException;
import hackaton.fastdisision.excaptions.WrongAdminPasswordException;
import hackaton.fastdisision.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private AdminService adminService;

    @Autowired
    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/getAdmin")
    public User requestAdminRole(@RequestBody(required = true) String clientAdminPassword, @AuthenticationPrincipal User currentUser) throws VotingNotFoundException, WrongAdminPasswordException {
        User user = adminService.checkAdminRequest(currentUser, clientAdminPassword);
        return user;
    }

}
