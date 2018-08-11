package org.foa.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author miaomuzhi
 * @since 2018/8/11
 */
@Embeddable
public class UserInfo {

    @Column(name = "account")
    private String account;

    @Column(name = "balance")
    private double balance;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
