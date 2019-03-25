/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 *
 * @author a.davydov
 */
public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError,?,Map<String,String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField()+"Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);       
    }
}
