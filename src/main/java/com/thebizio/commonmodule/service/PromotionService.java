package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.Promotion;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class PromotionService {
    final EntityManager entityManager;

    public PromotionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void incrementPromocodeCounter(Promotion promotion) {
        promotion.setTimesRedeemed(promotion.getTimesRedeemed()+1);
        entityManager.persist(promotion);
    }
}
