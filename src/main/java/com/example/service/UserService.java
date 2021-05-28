package com.example.service;

import com.example.dto.UserDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface UserService {
    List<UserDTO> findAllUsers();

    Optional<UserDTO> findUserById(String id) throws Exception;

    UserDTO saveUser(UserDTO userDTO) throws Exception;

    Map<String, Boolean> deleteUser(String userId) throws Exception;
}
