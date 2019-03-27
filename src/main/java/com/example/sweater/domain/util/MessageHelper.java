/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.domain.util;

import com.example.sweater.domain.User;

/**
 *
 * @author a.davydov
 */
public abstract class MessageHelper {
    public static String getAuthorName(User author){
        return author != null ? author.getUsername() : "<none>";
    }
}
