package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.UserResponseDto;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/updateProfilePic")
    public ResponseEntity<UserResponseDto> uploadProfilePicture(@ModelAttribute MultipartFile profilePicture ){
        return userService.uploadProfilePicture(profilePicture);
    }
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(){
        return userService.getUser();
    }

}
