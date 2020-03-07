package hackaton.fastdisision.controller;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:create-user-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:create-user-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MockMvc mockMvc;

    @Value("${admin.password}")
    private String rightAdminPassword;

    private String wrongAdminPassword = " wrongAdminPassword";

    private String adminUserId = "1";
    private String otherAdminUserId = "2";
    private String commonUserId = "3";
    private String otherCommonUserId = "4";

    private User adminUser;
    private User otherAdminUser;
    private User commonUser;
    private User otherCommonUser;

    @Test
    void giveAdmin() throws Exception {
        adminUser = userRepo.findById(adminUserId).get();
        mockMvc.perform(post("/admin/giveAdmin/" + commonUserId).with(user(adminUser)))
                .andDo(print())
                .andExpect(status().isOk());
        User adminedCommonUser = userRepo.findById(commonUserId).get();
        assertTrue(adminedCommonUser.getRoles().contains(UserRoles.ADMIN), "admin role was not given to common user!");
    }

    @Test
    void giveAdminDenied() throws Exception {
        commonUser = userRepo.findById(commonUserId).get();
        mockMvc.perform(post("/admin/giveAdmin/" + otherCommonUserId).with(user(commonUser)))
                .andDo(print())
                .andExpect(status().isForbidden());
        User adminedCommonUser = userRepo.findById(otherCommonUserId).get();
        assertFalse(adminedCommonUser.getRoles().contains(UserRoles.ADMIN), "give admin role request with wrong role was not denied!");
    }

    @Test
    void removeAdmin() throws Exception {
        adminUser = userRepo.findById(adminUserId).get();
        mockMvc.perform(post("/admin/removeAdmin/" + otherAdminUserId).with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(rightAdminPassword))
                .andDo(print())
                .andExpect(status().isOk());
        User unadminedCommonUser = userRepo.findById(otherAdminUserId).get();
        assertFalse(unadminedCommonUser.getRoles().contains(UserRoles.ADMIN), "admin role was not removed!");
    }

    @Test
    void removeAdminDeniedByPassword() throws Exception {
        adminUser = userRepo.findById(adminUserId).get();
        mockMvc.perform(post("/admin/removeAdmin/" + otherAdminUserId).with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongAdminPassword))
                .andDo(print())
                .andExpect(status().isForbidden());
        User unadminedCommonUser = userRepo.findById(otherAdminUserId).get();
        assertTrue(unadminedCommonUser.getRoles().contains(UserRoles.ADMIN), "admin remove request with wrong password was not denied!");
    }

    @Test
    void removeAdminDeniedByRole() throws Exception {
        commonUser = userRepo.findById(commonUserId).get();
        mockMvc.perform(post("/admin/removeAdmin/" + adminUserId).with(user(commonUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(rightAdminPassword))
                .andDo(print())
                .andExpect(status().isForbidden());
        User unadminedCommonUser = userRepo.findById(adminUserId).get();
        assertTrue(unadminedCommonUser.getRoles().contains(UserRoles.ADMIN), "admin remove request with wrong role was not denied!");
    }

}