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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:create-user-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:create-user-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MockMvc mockMvc;

    @Value("${admin.password}")
    private String rightAdminPassword;

    private String wrongAdminPassword = " wrongAdminPassword";

    private String commonUserId = "3";

    private User commonUser;

    @Test
    void requestAdmin() throws Exception {
        commonUser = userRepo.findById(commonUserId).get();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/getAdmin").with(user(commonUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(rightAdminPassword))
                .andDo(print())
                .andExpect(status().isOk());
        User adminedCommonUser = userRepo.findById(commonUserId).get();
        assertTrue(adminedCommonUser.getRoles().contains(UserRoles.ADMIN), "user did not get admin role!");
    }

    @Test
    void requestAdminWithoutUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/getAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongAdminPassword))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void requestAdminWrongPassword() throws Exception {
        commonUser = userRepo.findById(commonUserId).get();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/getAdmin").with(user(commonUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongAdminPassword))
                .andDo(print())
                .andExpect(status().isForbidden());
        User adminedCommonUser = userRepo.findById(commonUserId).get();
        assertFalse(adminedCommonUser.getRoles().contains(UserRoles.ADMIN), "admin request with wrong password was not denied!");
    }
}