package com.example.controllerTest;

import com.example.controller.UserController;
import com.example.dto.UserDTO;
import com.example.service.RelationshipService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private RelationshipService relationshipService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        UserDTO userDTOA = new UserDTO("jason@gmail.com");
        UserDTO userDTOB = new UserDTO("kati@gmail.com");
        List<UserDTO> userDTOList = Arrays.asList(userDTOA, userDTOB);
        given(userService.findAllUsers()).willReturn(userDTOList);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("jason@gmail.com")))
                .andExpect(jsonPath("$[1].email", is("kati@gmail.com")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));
        verify(userService, times(2)).findAllUsers();
        verifyNoMoreInteractions(userService);
    }
    @Test
    @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
    public void testGetAllUsersEmptyList() throws Exception {
        List<UserDTO> lstResult = new ArrayList<>();
        given(userService.findAllUsers()).willReturn(lstResult);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).findAllUsers();
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testGetUser() throws Exception {
        UserDTO userDTO = new UserDTO("jason@gmail.com");
        when(relationshipService.checkMail(userDTO.getEmail())).thenReturn(true);
        when(userService.findUserById(userDTO.getEmail())).thenReturn(Optional.of(userDTO));
        mockMvc.perform(get("/api/users/{id}", userDTO.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("jason@gmail.com")))
                .andExpect(content().contentType("application/json"));
        verify(userService, times(1)).findUserById(userDTO.getEmail());
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testGetUserWrongEmail() throws Exception {
        String email = "jason";
        when(relationshipService.checkMail(email)).thenReturn(false);
        mockMvc.perform(get("/api/users/{id}", email))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Email not accept")))
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO("jason@gmail.com");

        when(userService.saveUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);
        mockMvc.perform(post("/api/users")
                .content(asJsonString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email", is("jason@gmail.com")));
        verify(userService, times(1)).saveUser(userDTO);
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testCreateUserByEmailWrongFormat() throws Exception {
        UserDTO userDTO = new UserDTO("jason");

        when(userService.saveUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);
        mockMvc.perform(post("/api/users")
                .content(asJsonString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }
    @Test
    public void testDeleteUser() throws Exception {
        UserDTO userDTO = new UserDTO("jason@gmail.com");
        when(relationshipService.checkMail(userDTO.getEmail())).thenReturn(true);
        Map<String, Boolean> response = new HashMap<>();
        response.put("delete ok", Boolean.TRUE);
        when(userService.deleteUser(userDTO.getEmail())).thenReturn(response);
        mockMvc.perform(delete("/api/users/{id}", userDTO.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(userService, times(1)).deleteUser(userDTO.getEmail());
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testDeleteUserWrongEmail() throws Exception {
        String email = "jason";
        when(relationshipService.checkMail(email)).thenReturn(false);
        mockMvc.perform(delete("/api/users/{id}", email))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Email not accept")))
                .andExpect(content().contentType("application/json"));
    }
}
