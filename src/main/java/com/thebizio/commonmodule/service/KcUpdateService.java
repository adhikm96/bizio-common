package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.BillingAccount;
import com.thebizio.commonmodule.entity.KcUpdate;
import com.thebizio.commonmodule.entity.User;
import com.thebizio.commonmodule.enums.Status;
import com.thebizio.commonmodule.enums.TaskStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service("commonKcUpdateService")
public class KcUpdateService {

    final EntityManager entityManager;

    public KcUpdateService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public KcUpdate createKcUpdate(User user, Status status) {
        KcUpdate kcu = new KcUpdate();
        kcu.setStatus(TaskStatus.PENDING);
        kcu.setUser(user);
        kcu.setRequiredAction(status);
        entityManager.persist(kcu);
        return kcu;
    }

    public List<KcUpdate> getPendingUsers() {
        List<KcUpdate> kcUsers = (List<KcUpdate>) entityManager.createQuery("SELECT ku FROM KcUpdate ku WHERE ku.status = :status")
                .setParameter("status",TaskStatus.PENDING).getResultList();
        return kcUsers;
    }

    public Integer changeStatus(UUID kuId, TaskStatus status) {
        Integer count = entityManager.createQuery("update KcUpdate ku set ku.status = :status where ku.id = :id")
                .setParameter("status",status).setParameter("id",kuId).executeUpdate();

        entityManager.clear();
        entityManager.close();
        return count;
    }
}
