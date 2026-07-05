package com.cohabit.cohabitbackend.model;

import jakarta.persistence.*;

@Entity //it tells spring that this is a table that should be stored in mySQL
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //sql generates the id, not java
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String iitEmail;

    private String branch;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, unique = true)
    private String phoneNumber;


    public User() {
    }

    public User(String name, String iitEmail, String branch, Integer year, Gender gender, String phoneNumber) {
        this.name = name;
        this.iitEmail=iitEmail;
        this.branch = branch;
        this.year=year;
        this.gender=gender;
        this.phoneNumber=phoneNumber;
    }

    public String getName() {   //getters so another class can access it even tho its private
        return name;
    }

    public String getIitEmail(){
        return iitEmail;
    }

    public String getBranch() {
        return branch;
    }

    public Integer getYear(){
        return year;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIitEmail(String iitEmail) {
        this.iitEmail = iitEmail;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setName(String name) {
        this.name = name;
    }
}