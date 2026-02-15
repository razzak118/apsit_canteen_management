package com.apsit.canteen_management.repository;

import com.apsit.canteen_management.entity.OrderTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTicketRepository extends JpaRepository<OrderTicket, Long> {
}
