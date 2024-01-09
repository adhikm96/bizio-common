package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.BillingAccount;
import com.thebizio.commonmodule.entity.KcUpdate;
import com.thebizio.commonmodule.entity.User;
import com.thebizio.commonmodule.enums.TaskStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Service("commonKcUpdateService")
public class KcUpdateService {

    final EntityManager entityManager;

    public KcUpdateService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createKcUpdate(User user) {
        KcUpdate kcu = new KcUpdate();
        kcu.setStatus(TaskStatus.PENDING);
        kcu.setUser(user);
        entityManager.persist(kcu);
    }

    public List<KcUpdate> getPendingUsers() {
        List<KcUpdate> kcUsers = (List<KcUpdate>) entityManager.createQuery("SELECT ku FROM KcUpdate ku WHERE ku.status = :status")
                .setParameter("status",TaskStatus.PENDING).getResultList();
        return kcUsers;
    }

    public void changeStatus(UUID kuId, TaskStatus status) {
        entityManager.createQuery("update KcUpdate ku set ku.status = :status where ku.id = :id")
                .setParameter("status",status).setParameter("id",kuId).getResultList();
    }
}
