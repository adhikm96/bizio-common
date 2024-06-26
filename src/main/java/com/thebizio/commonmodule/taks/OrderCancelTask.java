package com.thebizio.commonmodule.taks;

import com.thebizio.commonmodule.entity.Order;
import com.thebizio.commonmodule.enums.OrderStatus;
import com.thebizio.commonmodule.service.OrderService;
import com.thebizio.commonmodule.service.PromotionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.TimerTask;
import java.util.UUID;

@AllArgsConstructor
public class OrderCancelTask extends TimerTask {

    private Logger logger = LoggerFactory.getLogger(OrderCancelTask.class);

    private OrderService orderService;

    private PromotionService promotionService;

    private UUID orderId;

    public OrderCancelTask(UUID orderId, OrderService orderService, PromotionService promotionService) {
        this.orderId = orderId;
        this.orderService = orderService;
        this.promotionService = promotionService;
    }

    @Override
    @Transactional
    public void run() {
        Order order = orderService.fetchById(orderId);

        if(order == null){
            logger.error("order not found " + orderId);
            return;
        }

        if(Arrays.asList(OrderStatus.COMPLETED, OrderStatus.EXPIRED).contains(order.getStatus())){
            return;
        }

        orderService.changeOrderStatus(order, OrderStatus.EXPIRED);

        if(order.getPromotion() != null){
            promotionService.decrementPromocodeCounter(order.getPromotion());
            logger.info(order.getPromotion().getCode() + " is incremented after expiring via timer task");
        }
    }

}
