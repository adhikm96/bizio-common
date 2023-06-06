package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.Order;
import com.thebizio.commonmodule.entity.OrderPayload;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service("commonOrderPayloadService")
public class OrderPayloadService {

    final EntityManager entityManager;

    public OrderPayloadService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public OrderPayload findByOrderRefNo(String orderRefNo) {
        OrderPayload op = (OrderPayload) entityManager.createQuery("SELECT op FROM OrderPayload op WHERE op.order.refNo = :orderRefNo")
                .setParameter("orderRefNo",orderRefNo).getSingleResult();
        return op;
    }
}
