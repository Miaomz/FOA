package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 本entity实现保存"我的期权池"的功能
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sid;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String optionCode;

}
