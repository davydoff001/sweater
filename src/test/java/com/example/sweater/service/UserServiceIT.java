/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author a.davydov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIT {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepo userRepo;
    
    @MockBean
    private MailSender mailSender;
    
    @Test
    public void testAddUser() {
        User user = new User();
        user.setEmail("some@gmail.com");
        boolean isUserCreated = userService.addUser(user);
        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
        
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }
    
    @Test
    public void addUserFailTest(){
        User user = new User();
        user.setUsername("John");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("John");
        
        boolean isUserCreated = userService.addUser(user);
        
        Assert.assertFalse(isUserCreated);
        
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0)).send(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }
    
    @Test
    public void activateUser(){
        User user = new User();
        user.setActivationCode("bingo!");
        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");
        
        boolean isUserActivated = userService.activateUser("activate");
        
        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());   
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }
    
    @Test
    public void activateUserFailTest(){
        boolean isUserActivated = userService.activateUser("activate me");      
        Assert.assertFalse(isUserActivated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}
