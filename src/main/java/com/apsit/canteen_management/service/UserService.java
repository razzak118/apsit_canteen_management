package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.UserResponseDto;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryServiceImpl cloudinaryService;

    public ResponseEntity<UserResponseDto> findByUsername(String username){
        return userRepository.findByUsername(username)
                .map(user-> modelMapper.map(user, UserResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserResponseDto> uploadProfilePicture(MultipartFile profilePicture) {
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map info=cloudinaryService.upload(profilePicture);
        user.setProfilePictureUrl(info.get("url").toString());
        return ResponseEntity.ok(modelMapper.map(userRepository.save(user),UserResponseDto.class));
    }

    public ResponseEntity<UserResponseDto> getUser() {
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(modelMapper.map(user,UserResponseDto.class));
    }
}
