package org.foa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author miaomuzhi
 * @since 2018/8/11
 */
@Data
@Embeddable
public class UserInfo {

    @Column(name = "account")
    private String account;

    @Column(name = "balance")
    private double balance;

}
