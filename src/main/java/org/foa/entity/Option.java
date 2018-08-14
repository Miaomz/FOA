package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * 注：所有百分比的取值均为0～1而非0～100
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Option {

    @Id
    private String optionCode;

    /**
     * 期权合约简称
     */
    private String optionAbbr;

    /**
     * 买量
     */
    private int bidVolume;

    /**
     * 买价
     */
    private double bidPrice;

    /**
     * 卖量
     */
    private int sellVolume;

    /**
     * 卖价
     */
    private double sellPrice;

    /**
     * 最新价
     */
    private double latestPrice;

    /**
     * 持仓量
     */
    private int position;

    /**
     * 涨跌幅
     */
    private double quoteChange;

    /**
     * 成交量
     */
    private int volume;

    /**
     * 振幅
     */
    private double amplitude;

    /**
     * 隐含波动率
     */
    private double interiorRange;

    /**
     * 行权价
     */
    private double execPrice;

    /**
     * 期权类型
     */
    private OptionType optionType;

    /**
     * 到期日
     */
    private LocalDate expireDay;

    /**
     * 合约乘数，默认10000
     */
    private double contractMultiplier = 10000;

    /**
     * 剩余自然日
     */
    private int remindedNaturalDays;

    /**
     * 剩余交易日
     */
    private int remindedBusinessDays;

    /**
     * 价值状态
     */
    private ValueState valueState;

    /**
     * 实际杠杆倍数
     */
    private double virtualLeverage;

    private double vega;

    private double gamma;

    private double theta;
}
