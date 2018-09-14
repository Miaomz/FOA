package org.foa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 注：所有百分比的取值均为0～1而非0～100
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Option_") //Option为MySQL关键字
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;

    /**
     * 期权代码，新浪财经api中用于获取期权数据，与optionAbbr一一对应
     */
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
    @JsonFormat
    @DateTimeFormat
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

    /**
     * 最新一次更新时间
     */
    @JsonFormat
    @DateTimeFormat
    private LocalDateTime time;
}
