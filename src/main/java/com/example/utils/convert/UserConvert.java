package com.example.utils.convert;

import com.example.dto.UserDTO;
import com.example.model.User;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserConvert {
    static ModelMapper modelMapper = new ModelMapper();
    public static UserDTO convertModelToDTO(User user){
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
    public static List<UserDTO> convertListModelToListDTO(List<User> listUser){
        return listUser.stream().map(ele -> modelMapper.map(ele, UserDTO.class)).collect(Collectors.toList());
    }
    public static User convertDTOToModel(UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }
}
