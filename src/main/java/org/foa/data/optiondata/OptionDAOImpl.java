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

    private boolean hasSubscribed(String optionCode, String userId) {
        Query query = entityManager.createQuery("select s from Subscription s where s.optionCode = :opt and s.userId = :usr");
        query.setParameter("opt", optionCode);
        query.setParameter("usr", userId);
        List<Subscription> res = query.getResultList();
        return res.size() == 0 ? false : true;
    }

    @Override
    public ResultMessage addInterestedOption(String optionCode, String userId) {
        if (hasSubscribed(optionCode, userId))
            return ResultMessage.FAILURE;
        else {
            Subscription subscription = new Subscription();
            subscription.setOptionCode(optionCode);
            subscription.setUserId(userId);
            entityManager.persist(subscription);
            return ResultMessage.SUCCESS;
        }
    }

    @Override
    @Modifying(clearAutomatically = true)
    public ResultMessage deleteInterestedOption(String optionCode, String userId) {
        return null;
    }
}
