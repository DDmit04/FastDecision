package hackaton.fastdisision.controller;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UserRepo userRepo;

    @Value("${spring.profiles.active:prod}")
    private String profile;

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) {
        boolean isDevMode = true;
        if(profile.equals("prod")) {
            isDevMode = false;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("isDevMode", isDevMode);
        return "index";
    }

}
