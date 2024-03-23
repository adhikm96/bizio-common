package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.Order;
import com.thebizio.commonmodule.enums.OrderStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.UUID;

@Service("commonOrderService")
public class OrderService {

    final EntityManager entityManager;

    public OrderService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Order fetchById(UUID orderId) {
        Order order = (Order) entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :orderId")
                .setParameter("orderId",orderId).getSingleResult();
        return order;
    }

    @Transactional
    public void changeOrderStatus(Order order, OrderStatus orderStatus) {
        entityManager.createQuery("update Order o set o.status = :status where o = :order")
                .setParameter("status", orderStatus).setParameter("order", order).executeUpdate();

        entityManager.clear();
        entityManager.close();
    }
}
