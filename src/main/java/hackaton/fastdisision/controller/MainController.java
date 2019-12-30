package hackaton.fastdisision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.views.VotingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    private final ObjectWriter writer;

    @Value("${spring.profiles.active:prod}")
    private String profile;

    @Autowired
    public MainController(ObjectMapper mapper) {
        this.writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(VotingView.MinimalData.class);
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        boolean isDevMode = true;
        if(profile.equals("prod")) {
            isDevMode = false;
        }
        String userAsString = null;
        if(user != null) {
            userAsString = writer.writeValueAsString(user);
        }
        model.addAttribute("currentUser", userAsString);
        model.addAttribute("isDevMode", isDevMode);
        return "index";
    }

}
