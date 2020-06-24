package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.excaptions.AccessDeniedException;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.service.intrface.AdminService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.2
 */
class AdminServiceTest extends BasicTest {

    @Value("${personalSettings.admin.password}")
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
        otherAdminUser.setRoles(new HashSet<>(Collections.singleton(UserRole.ADMIN)));
        adminUser.setRoles(new HashSet<>(Collections.singleton(UserRole.ADMIN)));
        commonUser.setRoles(new HashSet<>(Collections.singleton(UserRole.USER)));
    }

    @Test
    void checkAdminRequest() throws AccessDeniedException {
        adminService.getAdminRole(commonUser, rightAdminPassword);
        Mockito.verify(userRepo, times(1)).save(commonUser);
        assertTrue(commonUser.getRoles().contains(UserRole.ADMIN), "common user isn't get admin role!");
    }

    @Test
    void checkAdminRequestFail() {
        assertThrows(
                AccessDeniedException.class,
                () -> adminService.getAdminRole(
                        commonUser,
                        wrongAdminPassword
                ), "wrong password admin request was not denied!"
        );
    }

    @Test
    void giveAdmin() throws AccessDeniedException {
        adminService.giveAdminRole(commonUser, adminUser);
        Mockito.verify(userRepo, times(1)).save(commonUser);
        assertTrue(commonUser.getRoles().contains(UserRole.ADMIN), "admin isn't give common user an admin role!");
    }

    @Test
    void giveAdminFailed() {
        assertThrows(
                AccessDeniedException.class,
                () -> adminService.giveAdminRole(
                        adminUser, commonUser
                ), "give admin request was not denied!"
        );
    }

    @Test
    void removeAdmin() throws AccessDeniedException, AccessDeniedException {
        adminService.removeAdminRole(otherAdminUser, adminUser, rightAdminPassword);
        Mockito.verify(userRepo, times(1)).save(otherAdminUser);
        assertFalse(otherAdminUser.getRoles().contains(UserRole.ADMIN), "admin role was not removed!");
    }

    @Test
    void removeAdminFailedPassword() {
        commonUser.getRoles().add(UserRole.ADMIN);
        assertThrows(AccessDeniedException.class,
                () -> adminService.removeAdminRole(
                        otherAdminUser, adminUser, wrongAdminPassword
                ), "remove admin request with wrong password was not denied!"
        );
    }

    @Test
    void removeAdminFailedAccess() {
        assertThrows(AccessDeniedException.class,
                () -> adminService.removeAdminRole(
                        otherAdminUser, commonUser, rightAdminPassword
                ), "remove admin request with wrong role was not denied!"
        );
    }
}