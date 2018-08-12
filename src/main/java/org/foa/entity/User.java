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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column(nullable = false)
    private String password;

    private String role;

    @Embedded
    private UserInfo userInfo;

}
