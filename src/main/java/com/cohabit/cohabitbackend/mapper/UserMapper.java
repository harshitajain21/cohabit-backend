package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterUserRequest request) {

        User user = new User();

        user.setName(request.name());
        user.setIitEmail(request.iitEmail());
        user.setBranch(request.branch());
        user.setYear(request.year());
        user.setGender(request.gender());
        user.setPhoneNumber(request.phoneNumber());

        return user;
    }

    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getIitEmail(),
                user.getBranch(),
                user.getYear(),
                user.getGender()
        );
    }
}
