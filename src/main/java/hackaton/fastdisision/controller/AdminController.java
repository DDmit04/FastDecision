package hackaton.fastdisision.controller;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.WrongAdminPasswordException;
import hackaton.fastdisision.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/giveAdmin/{id}")
    public User giveAdminRole(@PathVariable("id") User user) throws AccessDeniedException {
        return adminService.giveAdmin(user);
    }

    @PostMapping("/removeAdmin/{id}")
    public User removeAdmin(@PathVariable("id") User user, @RequestBody(required = false) String clientAdminPassword, @AuthenticationPrincipal User currentUser) throws AccessDeniedException, WrongAdminPasswordException {
        if(clientAdminPassword == null) {
            clientAdminPassword = "";
        }
        return adminService.removeAdmin(user, currentUser, clientAdminPassword);
    }

}
