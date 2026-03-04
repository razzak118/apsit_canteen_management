package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.OrderItemDto;
import com.apsit.canteen_management.dto.OrderTicketDto;
import com.apsit.canteen_management.dto.PassChangeRequestDto;
import com.apsit.canteen_management.dto.UserResponseDto;
import com.apsit.canteen_management.entity.OrderTicket;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.repository.OrderTicketRepository;
import com.apsit.canteen_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryServiceImpl cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final OrderTicketRepository orderTicketRepository;

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

    public ResponseEntity<UserResponseDto> changePass(PassChangeRequestDto passChangeRequestDto){
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(passwordEncoder.matches(passChangeRequestDto.getOldPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(passChangeRequestDto.getNewPassword()));
            return ResponseEntity.ok(modelMapper.map(userRepository.save(user),UserResponseDto.class));
        }
        return ResponseEntity.badRequest().build();
    }
    public ResponseEntity<List<OrderTicketDto>> myOrders(){
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<OrderTicket>> myOrders=orderTicketRepository.findAllByUsername(user.getUsername());
        return myOrders
                .map(orders -> orders.stream()
                        .map(order -> modelMapper.map(order, OrderTicketDto.class))
                        .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(List.of()));
    }
}
