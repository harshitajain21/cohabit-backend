package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.exception.EmailAlreadyExistsException;
import com.cohabit.cohabitbackend.mapper.UserMapper;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserMapper userMap;

    public UserService(UserRepository userRepo,UserMapper userMap) { //If you want to create a UserController, you must give me a UserRepository (given by Spring)
        this.userRepo = userRepo;
        this.userMap=userMap;
    } // this is called DI: dependency injection - injecting the userRepositroy in service

    public UserResponse register(RegisterUserRequest newRequest) {

        //check if email already exists
        if (userRepo.existsByIitEmail(newRequest.iitEmail())) {
            throw new EmailAlreadyExistsException("An account with this IIT BHU email already exists."); //this stops the method immediately
        }

        User newuser = userMap.toEntity(newRequest);

        User savedUser = userRepo.save(newuser);

        return userMap.toResponse(savedUser);

    }

}
