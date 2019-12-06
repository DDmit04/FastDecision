package hackaton.fastdisision.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Value("${spring.profiles.active:prod}")
    private String profile;

    @GetMapping
    public String main(Model model) {
        boolean isDevMode = true;
        if(profile.equals("prod")) {
            isDevMode = false;
        }
        model.addAttribute("isDevMode", isDevMode);
        return "index";
    }

}
