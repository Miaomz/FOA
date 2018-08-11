package org.foa.data.userdata;

import org.foa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 王川源
 */
public interface UserDAO extends JpaRepository<User, String>, UserCustom{
}
