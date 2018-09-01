package org.foa.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foa.entity.Option;

/**
 * @author 王川源
 * 同一个标的物的两个合约
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Target {

    /**
     * 代表两个合约的共同简称
     * 如50ETF购9月2200，50ETF沽9月2200 则为50ETF9月2200 方便数据层查找
     * 前端若希望通过一个id对两个合约操作 则按改格式提交参数
     */
    String tid;

    /**
     * 看涨期权
     */
    Option optUp;

    /**
     * 看跌期权
     */
    Option optDown;

    /**
     * 行权价
     */
    double execPrice;

    public Target(Option optUp, Option optDown){
        this.optUp = optUp;
        this.optDown = optDown;
        this.execPrice = optUp.getExecPrice();
        String prefix = "50ETF";
        String suffix = optUp.getOptionAbbr().substring(5);
        this.tid = prefix + suffix;
    }

}
