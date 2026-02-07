package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.LoginRequestDto;
import com.apsit.canteen_management.dto.LoginResponseDto;
import com.apsit.canteen_management.dto.SignupRequestDto;
import com.apsit.canteen_management.dto.SignupResponseDto;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.repository.UserRepository;
import com.apsit.canteen_management.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        User user= (User) authentication.getPrincipal();
        assert user != null; // since user might be null !!!
        return new LoginResponseDto(authUtil.generateToken(user), user.getUserId());
    }

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if (user != null) throw new IllegalArgumentException("User already exist ! \n Try with different username.");
        user = userRepository.save(User.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .email(signupRequestDto.getUsername() + "@apsit.edu.in")
                .mobileNumber(signupRequestDto.getMobileNumber())
                .build()
        );
        return new SignupResponseDto(user.getUserId(), user.getUsername());
    }
}
