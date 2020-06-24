package hackaton.fastdisision.service;

import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.UserRole;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.service.intrface.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.3
 */
class UserServiceTest extends BasicTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static User adminTestingUser;
    private static User usualTestingUser;

    @BeforeEach
    public void initData() {

        usualTestingUser = new User();
        usualTestingUser.setId("1");
        usualTestingUser.setPassword("123");
        usualTestingUser.setRoles(new HashSet<>(Collections.singleton(UserRole.USER)));

        adminTestingUser = new User();
        adminTestingUser.setId("1");
        adminTestingUser.setPassword("123");
        adminTestingUser.setRoles(new HashSet<>(Collections.singleton(UserRole.ADMIN)));
    }

    @Test
    void addRoleToUser() {

        UserRole roleToGive = UserRole.ADMIN;

        userService.addRoleToUser(usualTestingUser, roleToGive);
        Mockito.verify(userRepo, times(1)).save(usualTestingUser);
        assertTrue(usualTestingUser.getRoles().contains(roleToGive), "New role mas not being added to user!");
    }

    @Test
    void addRoleToUserWithThisRole() {

        UserRole roleToGive = adminTestingUser.getRoles().iterator().next();

        userService.addRoleToUser(adminTestingUser, roleToGive);
        Mockito.verify(userRepo, times(0)).save(adminTestingUser);
    }

    @Test
    void removeRoleFromUser() {

        UserRole roleToRemove = adminTestingUser.getRoles().iterator().next();

        userService.removeRoleFromUser(adminTestingUser, roleToRemove);
        Mockito.verify(userRepo, times(1)).save(adminTestingUser);
        assertFalse(usualTestingUser.getRoles().contains(roleToRemove), "User role was not removed!");
    }

    @Test
    void removeRoleFromUserWithoutRole() {

        UserRole roleToRemove = UserRole.values()[0];
        adminTestingUser.setRoles(new HashSet<>());

        userService.removeRoleFromUser(adminTestingUser, roleToRemove);
        Mockito.verify(userRepo, times(0)).save(adminTestingUser);
        assertFalse(adminTestingUser.getRoles().contains(roleToRemove), "User must not have this role in any case!");
    }

    @Test
    void createUser() {

        String userPassword = adminTestingUser.getPassword();
        Mockito.when(passwordEncoder.encode(userPassword)).thenReturn(userPassword);

        userService.createUser(adminTestingUser);

        Mockito.verify(passwordEncoder, times(1)).encode(userPassword);
        Mockito.verify(userRepo, times(1)).save(adminTestingUser);
    }

    @Test
    void findById() {
        String idToSearch = "1";

        Mockito.when(userRepo.findById(idToSearch)).thenReturn(Optional.of(new User()));

        userService.findById(idToSearch);
        Mockito.verify(userRepo).findById(idToSearch);
    }

}