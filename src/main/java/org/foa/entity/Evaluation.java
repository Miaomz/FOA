package org.foa.entity;


import javax.persistence.Embeddable;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Embeddable
public class Evaluation {
    /**
     * |term1-term2|
     */
    private double difference;

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }
}
