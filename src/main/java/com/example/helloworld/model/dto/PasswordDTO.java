package com.example.helloworld.model.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String comfirmNewPassword;
}
