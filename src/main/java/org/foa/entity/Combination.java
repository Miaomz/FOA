package org.foa.entity;

import javax.persistence.*;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Entity
public class Combination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cid;

    @Column(nullable = false)
    private String opt1;

    @Column(nullable = false)
    private String opt2;

    /**
     * 暂时默认为1
     */
    private int purchaseNum;

    /**
     * 暂时定为属性
     */
    @Embedded
    private Evaluation evaluation;

    public Combination() {
    }

    public Combination(long cid, String opt1, String opt2, int purchaseNum, Evaluation evaluation) {
        this.cid = cid;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.purchaseNum = purchaseNum;
        this.evaluation = evaluation;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public int getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(int purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
