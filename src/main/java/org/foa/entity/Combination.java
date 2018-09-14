package org.foa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

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
     * 购入时间
     */
    @JsonFormat
    @DateTimeFormat
    private LocalDateTime time;

    /**
     * 组合的状态，收藏 或 购入 或 卖出
     * 一个组合正常生命周期为 游离(DEFAULT)->购入(PURCHASED)->SOLD(卖出)
     */
    private CombinationState state = CombinationState.DEFAULT;

    /**
     * 暂时定为属性
     */
    @Embedded
    private Evaluation evaluation;

    public Combination(String optUp1, String optDown1, String optUp2, String optDown2) {
        this.optUp1 = optUp1;
        this.optDown1 = optDown1;
        this.optUp2 = optUp2;
        this.optDown2 = optDown2;
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
