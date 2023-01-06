package com.svalero.workhub.util;

import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.WorkPlace;
import com.svalero.workhub.domain.dto.SpaceDTO;
import com.svalero.workhub.domain.dto.UserDTO;
import com.svalero.workhub.domain.dto.WorkPlaceDTO;

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

    public static WorkPlace workplaceMapper(WorkPlaceDTO workPlaceDTO){
        WorkPlace workPlace = new WorkPlace();
        workPlace.setName(workPlaceDTO.getName());
        workPlace.setDescription(workPlaceDTO.getDescription());
        workPlace.setPhoneNumber(workPlaceDTO.getPhoneNumber());
        workPlace.setCity(workPlaceDTO.getCity());
        workPlace.setAddress(workPlaceDTO.getAddress());
        workPlace.setSchedule(workPlaceDTO.getSchedule());
        return workPlace;
    }

    public static Space spaceMapper(SpaceDTO spaceDTO){
        Space space = new Space();
        space.setName(spaceDTO.getName());
        space.setDescription(spaceDTO.getDescription());
        space.setServices(spaceDTO.getServices());
        space.setWorkplace(spaceDTO.getWorkplace());
        return space;
    }
}
