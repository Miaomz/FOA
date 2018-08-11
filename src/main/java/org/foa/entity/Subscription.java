package org.foa.entity;

import javax.persistence.*;

/**
 * 本entity实现保存"我的期权池"的功能
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long sid;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String optionCode;

    public Subscription() {}

    public Subscription(long sid, String userId, String optionCode){
        this.sid = sid;
        this.userId = userId;
        this.optionCode = optionCode;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }
}
