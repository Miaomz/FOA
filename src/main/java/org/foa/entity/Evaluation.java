package org.foa.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Evaluation {

    private double term1;

    private double term2;

    /**
     * |term1-term2|
     */
    private double difference;

    /**
     * 期望可套利值
     */
    private double expectedEarning;

}
