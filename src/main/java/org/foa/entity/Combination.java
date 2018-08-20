package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Combination implements Comparable<Combination>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cid;

    /**
     * 用户
     */
    private String userId;

    /**
     * 看涨期权合约简称
     */
    @Column(nullable = false)
    private String optUp1;

    /**
     * 看跌期权合约简称
     */
    @Column(nullable = false)
    private String optDown1;

    /**
     * 看涨期权合约简称
     */
    @Column(nullable = false)
    private String optUp2;

    /**
     * 看跌期权合约简称
     */
    @Column(nullable = false)
    private String optDown2;

    /**
     * 暂时默认为1
     */
    private int purchaseNum = 1;

    /**
     * 暂时定为属性
     */
    @Embedded
    private Evaluation evaluation;

    public Combination(String optUp1, String optDown1, String optUp2, String optDown2, Evaluation evaluation) {
        this.optUp1 = optUp1;
        this.optDown1 = optDown1;
        this.optUp2 = optUp2;
        this.optDown2 = optDown2;
        this.evaluation = evaluation;
    }

    @Override
    public int compareTo(Combination o) {
        if (this.evaluation.getDifference() > o.evaluation.getDifference())
            return 1;
        else if (this.evaluation.getDifference() < o.evaluation.getDifference())
            return -1;
        else return 0;
    }
}
