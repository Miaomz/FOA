package org.foa.data.optiondata;

import org.foa.entity.Option;
import org.foa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 王川源
 */
public interface OptionDAO extends JpaRepository<Option, String>, OptionCustom{

    /**
     * 返回用户期权池内的所有期权
     * @param userId 用户账号
     * @return
     */
    @Query("select opt from Option opt where opt.optionCode in " +
            "(select distinct s.optionCode from Subscription s where s.userId = ?1)")
    List<Option> findInterestedOptions(String userId);

    /**
     * 返回对该期权感兴趣的所有用户
     * @param optionCode 期权编号
     * @return
     */
    @Query("select usr from User usr where usr.userId in " +
            "(select distinct s.userId from Subscription s where s.optionCode = ?1)")
    List<User> findInterestingUsers(String optionCode);
}
