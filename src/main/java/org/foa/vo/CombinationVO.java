package org.foa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foa.entity.Combination;
import org.foa.entity.CombinationState;
import org.foa.entity.Evaluation;
import org.foa.entity.Option;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationVO implements Comparable<CombinationVO>{

    private long cid;

    private String userId;

    private Option optUp1;

    private Option optDown1;

    private Option optUp2;

    private Option optDown2;

    private int purchaseNum = 1;

    @JsonFormat
    @DateTimeFormat
    private LocalDateTime time;

    /**
     * 价差
     */
    private double difference;

    public CombinationVO(Option optUp1, Option optDown1, Option optUp2, Option optDown2, double difference){
        this.optUp1 = optUp1;
        this.optDown1 = optDown1;
        this.optUp2 = optUp2;
        this.optDown2 = optDown2;
        this.difference = difference;
    }

    public CombinationVO(Combination combination){
        this.cid = combination.getCid();
        this.userId = combination.getUserId();
        this.purchaseNum = combination.getPurchaseNum();
        this.time = combination.getTime();
        this.difference = combination.getEvaluation().getDifference();
    }

    public Combination toCombination(){
        return new Combination(cid, userId, optUp1.getOptionAbbr(), optDown1.getOptionAbbr(), optUp2.getOptionAbbr(), optDown2.getOptionAbbr(), 1, time, CombinationState.DEFAULT, new Evaluation(difference, 0, difference, 0));
    }

    @Override
    public int compareTo(CombinationVO o) {
        if (this.getDifference() > o.getDifference())
            return 1;
        else if (this.getDifference() < o.getDifference())
            return -1;
        else return 0;
    }
}
