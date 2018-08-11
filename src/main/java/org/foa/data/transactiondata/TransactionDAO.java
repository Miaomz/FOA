package org.foa.data.transactiondata;

import org.foa.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 王川源
 */
public interface TransactionDAO extends JpaRepository<Transaction, Long>, TransactionCustom {
}
