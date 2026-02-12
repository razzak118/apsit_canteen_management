package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.UserResponseDto;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> findByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }


}
