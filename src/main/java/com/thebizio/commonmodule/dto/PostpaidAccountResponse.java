package com.thebizio.commonmodule.dto;

import com.thebizio.commonmodule.entity.Order;
import com.thebizio.commonmodule.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostpaidAccountResponse {

    private Order order;
    private User user;
}
