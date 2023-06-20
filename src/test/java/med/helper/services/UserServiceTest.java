package med.helper.services;

import med.helper.entitys.Authority;
import med.helper.entitys.User;
import med.helper.enums.ElementStatus;
import med.helper.repository.UserRepository;
import med.helper.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAdmins() {
        User admin1 = createUser(1L, "admin1@gmail.com", "password", ElementStatus.ACTIVE.getName(), "ADMIN");
        User admin2 = createUser(2L, "admin2@gmail.com", "password", ElementStatus.ACTIVE.getName(), "ADMIN");
        User user = createUser(3L, "user@gmail.com", "password", ElementStatus.ACTIVE.getName(), "USER");

        List<User> allUsers = new ArrayList<>();
        allUsers.add(admin1);
        allUsers.add(admin2);
        allUsers.add(user);

        when(userRepository.findAll()).thenReturn(allUsers);

        Set<User> admins = userService.getAllAdmins();

        assertEquals(2, admins.size());
        assertTrue(admins.contains(admin1));
        assertTrue(admins.contains(admin2));
    }



    @Test
    public void testDeactivateAdmin() {
        User admin = createUser(1L, "admin@gmail.com", "password", ElementStatus.ACTIVE.getName(), "ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertTrue(userService.deactivateAdmin(1L));
        assertEquals(ElementStatus.DELETED.getName(), admin.getStatus());

        verify(userRepository, times(1)).save(admin);
    }


    private User createUser(Long id, String email, String password, String status, String authority) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        Authority auth = new Authority();
        auth.setAuthority(authority);
        user.setAuthority(auth);
        user.setStatus(status);
        return user;
    }

}
