package org.foa.entity;


import lombok.Data;

import javax.persistence.Embeddable;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@Embeddable
public class Evaluation {
    /**
     * |term1-term2|
     */
    private double difference;

}
