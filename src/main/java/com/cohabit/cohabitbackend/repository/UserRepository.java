package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository        //save(), findAll(), delete(), findById() are all pre-written in JpaRespository
        extends JpaRepository<User, Long> {  //<table, type of primary key>

    /*Suppose you write: userRepository.findAll();
    Who ultimately executes the SQL query?: The implementation that Spring generated at runtime
    -> spring sees public interface _ extends_ -> Spring creates a hidden class (using proxies at runtime) that implements all the methods like: save(), findById(), findAll(), delete()
     */

    boolean existsByIitEmail(String iitEmail); //declared another method to check if this email already exists

    Optional<User> findByIitEmail(String iitEmail);

    List<User> findAllByGenderAndYear(Gender gender, Integer year);

}

//dependency injection: We never write new UserRepository() or try to make UserRepository object, still it is usable as an object because Spring creates a hidden class of UserRepository

