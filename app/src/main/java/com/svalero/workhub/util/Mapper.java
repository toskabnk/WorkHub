package com.svalero.workhub.util;

import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.dto.UserDTO;

public class Mapper {
    public static User userMapper(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setUsername(userDTO.getUsername());
        user.setEmail((userDTO.getEmail()));
        user.setPassword(userDTO.getPassword());
        user.setAdmin(userDTO.getAdmin());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }
}
