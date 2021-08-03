package com.example.contributoryloanapp.payload.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForgotPasswordResponse {
    private Integer status;
    private String message;
}
