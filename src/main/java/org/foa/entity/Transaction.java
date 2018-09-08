package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    public static final double FEE_RATE = 0.01;//暂定手续费比例为1%


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tid;

    private String userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 合约名称
     */
    private String optionAbbr;

    /**
     * 成交数量
     */
    private int quantity;

    /**
     * 成交价
     */
    private double price;

    /**
     * 买卖方向
     */
    private TransactionDirection transactionDirection;

    /**
     * 交易类型
     */
    private TransactionType transactionType;

    /**
     * @return 手续费
     */
    public double getFee(){
        return getSum() * FEE_RATE;
    }

    /**
     * @return 成交额
     */
    public double getSum(){
        return quantity * price;
    }
}
