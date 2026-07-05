package com.cohabit.cohabitbackend.controller; //just tells the location, that its in package controller

import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController    //this means that this class handles HTTP requests
@RequestMapping("/api/users")    //base url.. url is http://localhost:8080/api/users/
public class UserController {

    private final UserService user_ser;
    public UserController(UserService user_ser) {
        this.user_ser = user_ser;
    } //DI: Injecting user service in here

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(  //response entity represents full HTTP response
            @Valid
            @RequestBody   //annotation to depict that following is the body of HTTP request
            RegisterUserRequest new_request) {    //parameter is an object : user

        UserResponse response = user_ser.register(new_request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    }





