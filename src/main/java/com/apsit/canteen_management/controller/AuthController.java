package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.LoginRequestDto;
import com.apsit.canteen_management.dto.LoginResponseDto;
import com.apsit.canteen_management.dto.SignupRequestDto;
import com.apsit.canteen_management.dto.SignupResponseDto;
import com.apsit.canteen_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){ // using LoginRequestDto for now as SignupRequestDto will also have same fields.
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }


}
