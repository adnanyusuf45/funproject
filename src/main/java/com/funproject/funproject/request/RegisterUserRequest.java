package com.funproject.funproject.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterUserRequest {
    private String username;
    private String password;
}