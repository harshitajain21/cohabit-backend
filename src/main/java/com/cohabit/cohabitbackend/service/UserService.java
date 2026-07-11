package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.exception.EmailAlreadyExistsException;
import com.cohabit.cohabitbackend.mapper.UserMapper;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.UserRepository;
import com.cohabit.cohabitbackend.util.IitEmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserMapper userMap;
    private final PasswordEncoder passwordEncoder;
    private final IitEmailValidator iitEmailValidator;

    public UserService(
            UserRepository userRepo,
            UserMapper userMap,
            PasswordEncoder passwordEncoder,
            IitEmailValidator iitEmailValidator
    ) { //If you want to create a UserController, you must give me a UserRepository (given by Spring)
        this.userRepo = userRepo;
        this.userMap=userMap;
        this.passwordEncoder = passwordEncoder;
        this.iitEmailValidator = iitEmailValidator;
    } // this is called DI: dependency injection - injecting the userRepositroy in service

    public UserResponse register(RegisterUserRequest newRequest) {

        //check if email already exists
        String normalizedEmail = iitEmailValidator.normalize(newRequest.iitEmail());
        if (userRepo.existsByIitEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("An account with this IIT BHU email already exists."); //this stops the method immediately
        }

        User newuser = userMap.toEntity(newRequest);
        newuser.setIitEmail(normalizedEmail);
        newuser.setPassword(passwordEncoder.encode(newRequest.password()));
        newuser.setEmailVerified(false);

        User savedUser = userRepo.save(newuser);

        return userMap.toResponse(savedUser);

    }

}
