package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    public static final String ROLE_USER = "ROLE_USER";

    @Id
    private String userId;

    @Column(nullable = false)
    private String password;

    private String role;

    @Embedded
    private UserInfo userInfo;

}
