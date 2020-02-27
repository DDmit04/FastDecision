package hackaton.fastdisision.service;

import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.WrongAdminPasswordException;
import hackaton.fastdisision.repo.UserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminServiceTest {

    @Value("${admin.password}")
    private String rightAdminPassword;
    private String wrongAdminPassword = " wrongAdminPassword";

    @Autowired
    private AdminService adminService;

    @MockBean
    private UserRepo userRepo;

    private static User adminUser;
    private static User otherAdminUser;
    private static User commonUser;

    @BeforeAll
    public static void initData() {
        adminUser = new User();
        adminUser.setId("1");
        adminUser.setUsername("1");
        adminUser.setPassword("1");
        commonUser = new User();
        commonUser.setId("2");
        commonUser.setUsername("2");
        commonUser.setPassword("2");
        otherAdminUser = new User();
        otherAdminUser.setId("3");
        otherAdminUser.setUsername("3");
        otherAdminUser.setPassword("3");
    }

    @BeforeEach
    void refreshTestData() {
        otherAdminUser.setRoles(new HashSet<>(Collections.singleton(UserRoles.ADMIN)));
        adminUser.setRoles(new HashSet<>(Collections.singleton(UserRoles.ADMIN)));
        commonUser.setRoles(new HashSet<>(Collections.singleton(UserRoles.USER)));
    }

    @Test
    void checkAdminRequest() throws WrongAdminPasswordException {
        adminService.checkAdminRequest(commonUser, rightAdminPassword);
        assertTrue(commonUser.getRoles().contains(UserRoles.ADMIN));
    }

    @Test
    void checkAdminRequestFail() {
        assertThrows(
                WrongAdminPasswordException.class,
                () -> adminService.checkAdminRequest(
                        commonUser,
                        wrongAdminPassword
                )
        );
    }

    @Test
    void giveAdmin() throws AccessDeniedException {
        adminService.giveAdmin(commonUser);
        assertTrue(commonUser.getRoles().contains(UserRoles.ADMIN));
    }

    @Test
    void giveAdminFailed() {
        assertThrows(
                AccessDeniedException.class,
                () -> adminService.giveAdmin(
                        adminUser
                )
        );
    }

    @Test
    void removeAdmin() throws AccessDeniedException, WrongAdminPasswordException {
        adminService.removeAdmin(otherAdminUser, adminUser, rightAdminPassword);
        assertFalse(otherAdminUser.getRoles().contains(UserRoles.ADMIN));
    }

    @Test
    void removeAdminFailedPassword() {
        commonUser.getRoles().add(UserRoles.ADMIN);
        assertThrows(WrongAdminPasswordException.class,
                () -> adminService.removeAdmin(
                        otherAdminUser, adminUser, wrongAdminPassword
                )
        );
    }

    @Test
    void removeAdminFailedAccess() {
        assertThrows(AccessDeniedException.class,
                () -> adminService.removeAdmin(
                        otherAdminUser, commonUser, rightAdminPassword
                )
        );
    }
}