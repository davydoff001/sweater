/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author a.davydov
 */
public enum Role implements GrantedAuthority{
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
