package com.ISCES.service;


import com.ISCES.entities.User;
import com.ISCES.repository.UserRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Setter
@Service
public class UserService {
    private UserRepo userRepo;



    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Transactional
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @Transactional
    public User findByEmail(String email){
        return userRepo.findByEmail(email);
    }




}
