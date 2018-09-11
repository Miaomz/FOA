package org.foa.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王川源
 * 50ETF的实时行情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {

    /**
     * 名称
     */
    String name = "50ETF";

    /**
     * 今开
     */
    double openingPrice;

    /**
     * 昨收
     */
    double closingPrice;

    /**
     * 最新价
     */
    double latestPrice;

    /**
     * 最高价
     */
    double highestPrice;

    /**
     * 最低价
     */
    double lowestPrice;

    /**
     * 成交量
     */
    int quantity;

    /**
     * 成交额
     */
    double sum;

}
