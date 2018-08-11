package org.foa.data.combinationdata;

import org.foa.entity.Combination;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 王川源
 */
public interface CombinationDAO extends JpaRepository<Combination, Long>, CombinationCustom{
}
