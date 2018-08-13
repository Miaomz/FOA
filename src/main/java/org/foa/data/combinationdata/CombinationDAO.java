package org.foa.data.combinationdata;

import org.foa.entity.Combination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 王川源
 */
public interface CombinationDAO extends JpaRepository<Combination, Long>, CombinationCustom{

    /**
     * 按盈利指标排序 即term1-term2的绝对值降序排列
     * @return
     */
    List<Combination> findAllByOrderByEvaluationDifferenceDesc();
    
}
