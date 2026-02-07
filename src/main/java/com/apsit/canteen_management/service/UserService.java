package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.UserResponseDto;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<UserResponseDto> findByUsername(String username){
        return userRepository.findByUsername(username)
                .map(user-> modelMapper.map(user, UserResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }
}
