package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.BillingAccount;
import com.thebizio.commonmodule.entity.Promotion;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service("commonBillingAccountService")
public class BillingAccountService {
    final EntityManager entityManager;

    public BillingAccountService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<BillingAccount> getBillingAccountByStripeId(String stripePaymentMethodId) {
        List<BillingAccount> billingAccount = (List<BillingAccount>) entityManager.createQuery("SELECT ba FROM BillingAccount ba WHERE ba.stripePaymentMethodId = :stripeId")
                .setParameter("stripeId","BA123").getResultList();
        return billingAccount;
    }
}
