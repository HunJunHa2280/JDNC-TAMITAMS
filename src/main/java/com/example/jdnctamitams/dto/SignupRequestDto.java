package com.example.jdnctamitams.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Pattern(message = "유저이름 똑바로 하세요",regexp = "^[a-z0-9]{4,10}$")
    private String username;
    @NotBlank
    @Pattern(message = "비밀번호 똑바로 하세요",regexp = "^[a-z0-9A-Z]{8,15}$")
    private String password;
    private boolean admin = false;
    private String adminToken ="";
}