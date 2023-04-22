package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.*;

import javax.validation.constraints.NotNull;

public interface IOrderFlow {
    Order createOrder(@NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion);
    String checkout(Order order);
}
