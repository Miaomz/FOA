package org.foa.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foa.entity.Combination;
import org.foa.entity.Option;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinationVO {

    private long cid;

    private String userId;

    private Option optUp1;

    private Option optDown1;

    private Option optUp2;

    private Option optDown2;

    private int purchaseNum;

    private LocalDateTime time;

    public CombinationVO(Combination combination){
        this.cid = combination.getCid();
        this.userId = combination.getUserId();
        this.purchaseNum = combination.getPurchaseNum();
        this.time = combination.getTime();
    }
}
