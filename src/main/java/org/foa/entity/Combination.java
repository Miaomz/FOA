package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private int purchaseNum = 1;

    /**
     * 暂时定为属性
     */
    @Embedded
    private Evaluation evaluation;

}
