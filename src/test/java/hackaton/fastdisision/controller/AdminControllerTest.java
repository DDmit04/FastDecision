package hackaton.fastdisision.controller;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 */
@AutoConfigureMockMvc
@Sql(scripts = "classpath:create-user-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:create-user-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AdminControllerTest extends BasicTest {

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
    private User commonUser;

    @Test
    void giveAdmin() throws Exception {
        adminUser = userRepo.findById(adminUserId).get();
        mockMvc.perform(post("/admin/giveAdmin/{id}", commonUserId).with(user(adminUser)))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(parameterWithName("id")
                                .description("ID of user to give admin.")),
                        responseFields(
                                fieldWithPath("id")
                                        .description("ID of admined user."),
                                fieldWithPath("username")
                                        .description("Username of admined user."),
                                fieldWithPath("roles")
                                        .description("roles of unadmined user."),
                                fieldWithPath("userPic")
                                        .type(String.class)
                                        .description("unadmined user avatar link.")
                        )
                ))
                .andExpect(status().isOk());

        User adminedCommonUser = userRepo.findById(commonUserId).get();
        assertTrue(adminedCommonUser.isAdmin(), "admin role was not given to common user!");
    }

    @Test
    void giveAdminDenied() throws Exception {
        commonUser = userRepo.findById(commonUserId).get();
        mockMvc.perform(post("/admin/giveAdmin/{id}", otherCommonUserId).with(user(commonUser)))
                .andDo(print())
                .andExpect(status().isForbidden());

        User adminedCommonUser = userRepo.findById(otherCommonUserId).get();
        assertFalse(adminedCommonUser.isAdmin(), "give admin role request with wrong role was not denied!");
    }

    @Test
    void removeAdmin() throws Exception {
        adminUser = userRepo.findById(adminUserId).get();
        mockMvc.perform(post("/admin/removeAdmin/{id}", otherAdminUserId).with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(rightAdminPassword))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id")
                                        .description("ID of user to remove admin.")),
                        responseFields(
                                fieldWithPath("id")
                                        .description("ID of unadmined user."),
                                fieldWithPath("username")
                                        .description("Username of unadmined user."),
                                fieldWithPath("roles")
                                        .description("roles of unadmined user."),
                                fieldWithPath("userPic")
                                        .type(String.class)
                                        .description("unadmined user avatar link.")
                                )
                ))
                .andExpect(status().isOk());

        User unadminedCommonUser = userRepo.findById(otherAdminUserId).get();
        assertFalse(unadminedCommonUser.isAdmin(), "admin role was not removed!");
        assertTrue(unadminedCommonUser.getRoles().contains(UserRole.USER), "unadmined user must be common user too!");
    }

    @Test
    void removeAdminDeniedByPassword() throws Exception {
        adminUser = userRepo.findById(adminUserId).get();
        mockMvc.perform(post("/admin/removeAdmin/{id}", otherAdminUserId).with(user(adminUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongAdminPassword))
                .andDo(print())
                .andExpect(status().isForbidden());

        User unadminedCommonUser = userRepo.findById(otherAdminUserId).get();
        assertTrue(unadminedCommonUser.isAdmin(), "admin remove request with wrong password was not denied!");
    }

    @Test
    void removeAdminDeniedByRole() throws Exception {
        commonUser = userRepo.findById(commonUserId).get();
        mockMvc.perform(post("/admin/removeAdmin/{id}", adminUserId).with(user(commonUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(rightAdminPassword))
                .andDo(print())
                .andExpect(status().isForbidden());

        User unadminedCommonUser = userRepo.findById(adminUserId).get();
        assertTrue(unadminedCommonUser.isAdmin(), "admin remove request with wrong role was not denied!");
    }

}