package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRoles;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.excaptions.WrongAdminPasswordException;
import hackaton.fastdisision.repo.UserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest extends BasicTest {

    @Value("${admin.password}")
    private String rightAdminPassword;

    private String wrongAdminPassword = "wrongAdminPassword";

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
        assertTrue(commonUser.getRoles().contains(UserRoles.ADMIN), "common user isn't get admin role!");
    }

    @Test
    void checkAdminRequestFail() {
        assertThrows(
                WrongAdminPasswordException.class,
                () -> adminService.checkAdminRequest(
                        commonUser,
                        wrongAdminPassword
                ), "wrong password admin request was not denied!"
        );
    }

    @Test
    void giveAdmin() throws AccessDeniedException {
        adminService.giveAdmin(commonUser, adminUser);
        assertTrue(commonUser.getRoles().contains(UserRoles.ADMIN), "admin isn't give common user an admin role!");
    }

    @Test
    void giveAdminFailed() {
        assertThrows(
                AccessDeniedException.class,
                () -> adminService.giveAdmin(
                        adminUser, commonUser
                ), "give admin request was not denied!"
        );
    }

    @Test
    void removeAdmin() throws AccessDeniedException, WrongAdminPasswordException {
        adminService.removeAdmin(otherAdminUser, adminUser, rightAdminPassword);
        assertFalse(otherAdminUser.getRoles().contains(UserRoles.ADMIN), "admin role was not removed!");
    }

    @Test
    void removeAdminFailedPassword() {
        commonUser.getRoles().add(UserRoles.ADMIN);
        assertThrows(WrongAdminPasswordException.class,
                () -> adminService.removeAdmin(
                        otherAdminUser, adminUser, wrongAdminPassword
                ), "remove admin request with wrong password was not denied!"
        );
    }

    @Test
    void removeAdminFailedAccess() {
        assertThrows(AccessDeniedException.class,
                () -> adminService.removeAdmin(
                        otherAdminUser, commonUser, rightAdminPassword
                ), "remove admin request with wrong role was not denied!"
        );
    }
}