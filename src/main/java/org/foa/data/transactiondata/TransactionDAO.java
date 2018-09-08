package org.foa.data.transactiondata;

import org.foa.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 王川源
 */
public interface TransactionDAO extends JpaRepository<Transaction, Long>, TransactionCustom {

    /*
     * 需要排序的返回所有交易
     * findAll(SortUtil.sortBy(SortDTO... dtos))
     *
     * dto = new SortDTO(String transactionType, String orderField)
     * transactionType desc表示降序 asc表示升序
     * orderField 为实体类中需要比较的字段
     * dto = new SortDTO(String orderField)
     * transactionType 默认为降序
     *
     * SortUtil.sortBy 传入的参数为SortDTO的变长列表 排序优先级从左往右降低
     *
     * 示例
     * 在TransactionDAO中
     * 按日期降序排列返回所有交易
     * findAll(SortUtil.sortBy(new SortDTO("desc", "time")))
     * 按日期升序排列返回所有交易
     * findAll(SortUtil.sortBy(new SortDTO("asc", "time")))
     * 按盈利降序排列返回所有交易
     * findAll(SortUtil.sortBy(new SortDTO("desc", "profit")))
     * 先按日期降序，再按盈利降序返回所有交易
     * findAll(SortUtil.sortBy(new SortDTO("desc", "time"), new SortDTO("desc", "profit")))
     */

    /*
     * 下面是三个简易的排序返回接口
     */

    /**
     * 按日期降序排列返回所有交易
     * @return
     */
    List<Transaction> findAllByOrderByTimeDesc();

    /**
     * 按日期升序排列返回所有交易
     * @return
     */
    List<Transaction> findAllByOrderByTimeAsc();

    /**
     * 按盈利降序排列返回所有交易
     * @return
     */
    List<Transaction> findAllByOrderByProfitDesc();

    /**
     * 返回用户的全部交易，按时间降序
     * @param userId 用户Id
     * @return
     */
    List<Transaction> findByUserIdOrderByTimeDesc(String userId);

}
