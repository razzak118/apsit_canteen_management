package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.OrderTicketDto;
import com.apsit.canteen_management.entity.OrderTicket;
import com.apsit.canteen_management.enums.OrderStatus;
import com.apsit.canteen_management.repository.OrderTicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminOrderService {
    private final OrderTicketRepository orderTicketRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<Page<OrderTicketDto>> getOrderByOrderStatus(OrderStatus orderStatus,int pageNo){
        Sort sort=orderStatus==OrderStatus.PENDING
                ? Sort.by(Sort.Direction.ASC,"createdAt")
                : Sort.by(Sort.Direction.DESC, "updatedAt");
        return ResponseEntity.ok(
                orderTicketRepository.findByOrderStatus(
                            orderStatus,
                            PageRequest.of(pageNo,20,sort)
                        )
                        .map(orderTicket -> modelMapper.map(orderTicket, OrderTicketDto.class))
                );
    }
    @Transactional
    public ResponseEntity<?> acceptPendingOrder(Long orderId){
        OrderTicket orderTicket=orderTicketRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("order does not exist to accept. Try with another one"));
        if(orderTicket.getOrderStatus()==OrderStatus.PENDING){
            orderTicket.setOrderStatus(OrderStatus.IN_PROGRESS);
            orderTicket.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(modelMapper.map(orderTicketRepository.save(orderTicket),OrderTicketDto.class));
        }
        return ResponseEntity.badRequest().build();
    }
    @Transactional
    public ResponseEntity<?> markOrderReady(Long orderId){
        OrderTicket orderTicket=orderTicketRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Can't find this order to make it ready!"));
        if(orderTicket.getOrderStatus()!=OrderStatus.IN_PROGRESS){
            return ResponseEntity.badRequest().build();
        }
        orderTicket.setOrderStatus(OrderStatus.READY);
        orderTicket.setOrderToken(UUID.randomUUID().toString());
        orderTicket.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(modelMapper.map(orderTicketRepository.save(orderTicket), OrderTicketDto.class));
    }
    @Transactional
    public ResponseEntity<?> verifyAndClaimOrder(String orderToken){
        OrderTicket orderTicket=orderTicketRepository.findByOrderToken(orderToken)
                .orElseThrow(()->new IllegalArgumentException("Invalid QR code"));
        if(orderTicket.getOrderStatus()==OrderStatus.DELIVERED){
            return ResponseEntity.badRequest().body("Order already is claimed");
        }
        if(orderTicket.getOrderStatus()!=OrderStatus.READY){
            return ResponseEntity.badRequest().body("Order is NOT ready yet.");
        }
        orderTicket.setOrderStatus(OrderStatus.DELIVERED);
        orderTicket.setUpdatedAt(LocalDateTime.now());
        orderTicketRepository.save(orderTicket);
        return ResponseEntity.ok("Claimed Successfully");
    }
    @Transactional
    public ResponseEntity<?> rejectOrder(Long orderId){
        OrderTicket orderTicket=orderTicketRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("order does not exist to cancel"));
        orderTicket.setOrderStatus(OrderStatus.CANCELLED);
        orderTicket.setUpdatedAt(LocalDateTime.now());

        // Write payments logic here

        return ResponseEntity.ok(orderTicketRepository.save(orderTicket));
    }
    public long countByOrderStatus(OrderStatus orderStatus){
        return orderTicketRepository.countByOrderStatus(orderStatus);
    }
    public long countOrdersCompletedToday(){
        LocalDateTime now=LocalDateTime.now();
        return orderTicketRepository.countByOrderStatusAndCompletedAtBetween(
                OrderStatus.DELIVERED,now.toLocalDate().atStartOfDay(),now
        );
    }

}
