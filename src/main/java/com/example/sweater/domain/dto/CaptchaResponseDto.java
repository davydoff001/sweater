/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

/**
 *
 * @author a.davydov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {
    private boolean success;
    @JsonAlias("error-codes")
    private Set<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
    
}
