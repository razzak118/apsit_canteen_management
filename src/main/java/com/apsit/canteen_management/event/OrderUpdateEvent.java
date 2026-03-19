package com.apsit.canteen_management.event;

import com.apsit.canteen_management.dto.OrderTicketDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderUpdateEvent {
    private final String username;
    private final OrderTicketDto orderTicketDto;
}
