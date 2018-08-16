package org.foa.data.optiondata;

import org.foa.entity.Subscription;
import org.foa.util.ResultMessage;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author 王川源
 */
public class OptionDAOImpl implements OptionCustom {

    @PersistenceContext
    EntityManager entityManager;

    private boolean hasSubscribed(String optionAbbr, String userId) {
        Query query = entityManager.createQuery("select s from Subscription s where s.optionAbbr = :opt and s.userId = :usr");
        query.setParameter("opt", optionAbbr);
        query.setParameter("usr", userId);
        List<Subscription> res = query.getResultList();
        return res.size() == 0 ? false : true;
    }

    @Override
    public ResultMessage addInterestedOption(String optionAbbr, String userId) {
        if (hasSubscribed(optionAbbr, userId))
            return ResultMessage.FAILURE;
        else {
            Subscription subscription = new Subscription();
            subscription.setOptionAbbr(optionAbbr);
            subscription.setUserId(userId);
            entityManager.persist(subscription);
            return ResultMessage.SUCCESS;
        }
    }

    @Override
    @Modifying(clearAutomatically = true)
    public ResultMessage deleteInterestedOption(String optionAbbr, String userId) {
        Query query = entityManager.createQuery("delete from Subscription s where s.optionAbbr = :opt and s.userId = :usr");
        query.setParameter("opt", optionAbbr);
        query.setParameter("usr", userId);
        int res = query.executeUpdate();
        return res > 0 ? ResultMessage.SUCCESS : ResultMessage.FAILURE;
    }
}
