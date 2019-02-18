/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.repos;

import com.example.sweater.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author a.davydov
 */
public interface UserRepo extends JpaRepository<User, Long>{
    User findByUsername(String username);

    public User findByActivationCode(String code);
}
