package com.example.serviceTest;

import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import com.example.utils.convert.UserConvert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @TestConfiguration
    public static class UserServiceTestConfig{
        @Bean
        UserService userService(){
            return new UserServiceImpl();
        }
    }

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private UserConvert userConvert;
    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User("jason@gmail.com");
        User user1 = new User("kati@gmail.com");

        List<User> userListExpected = Arrays.asList(user, user1);

        when(userRepository.findAll()).thenReturn(userListExpected);
        List<UserDTO> userListActual = userService.findAllUsers();
        assertEquals(userListExpected.size(), userListActual.size());
    }

    @Test
    public void testGetUserById() throws Exception {
        User userExpected = new User("jason@gmail.com");
        when(userRepository.findById(userExpected.getEmail())).thenReturn(Optional.of(userExpected));
        Optional<UserDTO> userActual = userService.findUserById(userExpected.getEmail());
        assertEquals(userExpected.getEmail(), userActual.get().getEmail());
    }
    @Test
    public void testGetUserByIdNotExists() throws Exception {
        User user = new User("jason@gmail.com");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> userService.findUserById(user.getEmail()));
        assertEquals("Error not found", exception.getMessage());
    }
    @Test
    public void testCreateUser() throws Exception {
        User user = new User("jason@gmail.com");
        when(userRepository.save(user)).thenReturn(user);
        UserDTO userDTO = userService.saveUser(UserConvert.convertModelToDTO(user));
        assertEquals(user.getEmail(), userDTO.getEmail());
    }
    @Test
    public void testCreateUserAlready() throws Exception {
        User user = new User("jason@gmail.com");
        List<User> lstTest = new ArrayList<>();
        lstTest.add(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.existsById(user.getEmail())).thenReturn(true);
        Throwable exception = assertThrows(Exception.class, () -> userService.saveUser(UserConvert.convertModelToDTO(user)));
        assertEquals("Error cause this email exists", exception.getMessage());
    }
    @Test
    public void testDeleteUser() throws Exception {
        User user = new User("jason@gmail.com");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        Map<String, Boolean> map = userService.deleteUser(user.getEmail());
        assertEquals(true,map.get("Delete user success"));
        verify(userRepository, times(1)).findById(user.getEmail());
        verify(userRepository, times(1)).delete(user);
    }
    @Test
    public void testDeleteUserFailByExists() throws Exception {
        User user = new User("jason@gmail.com");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> userService.deleteUser(user.getEmail()));
        assertEquals("Error cause this email not exists", exception.getMessage());
    }
}
