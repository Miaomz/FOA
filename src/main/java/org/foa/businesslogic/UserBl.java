package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.data.userdata.UserDAO;
import org.foa.entity.User;
import org.foa.entity.UserInfo;
import org.foa.security.MD5Encrypt;
import org.foa.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PersistenceException;

@RestController
@RequestMapping("/UserBl")
public class UserBl {

    private Gson gson = new Gson();

    @Autowired
    private UserDAO userDAO;

    /**
     *
     * @param username id and name of user
     * @param password password that user inputs
     * @return resultMessage
     */
    @RequestMapping("/login")
    private ResultMessage login(@RequestParam String username, @RequestParam String password) {
        try {
            User targetUser = userDAO.getOne(username);

            if (targetUser.getUserId() == null){//it seems that the return value of JpaRepository will never be null
                return ResultMessage.INEXISTENCE;
            }

            String truePassword = targetUser.getPassword();
            if (MD5Encrypt.MD5(password).equals(truePassword)){
                return ResultMessage.SUCCESS;
            } else {
                return ResultMessage.FAILURE;
            }
        } catch (PersistenceException e){
            return ResultMessage.FAILURE;
        }
    }

    /**
     *
     * @param username id and name of user
     * @param firstPassword first password that user inputs
     * @param repetitiousPassword second time to confirm
     * @return resultMessage
     */
    @RequestMapping("/signUp")
    private ResultMessage signUp(@RequestParam String username ,@RequestParam String firstPassword, @RequestParam String repetitiousPassword){
        if (firstPassword == null || !firstPassword.equals(repetitiousPassword)){
            return ResultMessage.FAILURE;
        }

        try {
            User user = new User(username, MD5Encrypt.MD5(firstPassword), User.ROLE_USER, new UserInfo());
            userDAO.saveAndFlush(user);
        } catch (DataAccessException |PersistenceException e){
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }

    /**
     *
     * @param username name and id
     * @param originalPassword old password that user inputs
     * @param newPassword new password that user wants to use
     * @return resultMessage
     */
    @RequestMapping("/modifyPassword")
    private ResultMessage modifyPassword(@RequestParam String username ,@RequestParam String originalPassword, @RequestParam String newPassword){
        try {
            String truePassword = userDAO.getOne(username).getPassword();
            if (truePassword == null || !truePassword.equals(MD5Encrypt.MD5(originalPassword))){
                return ResultMessage.FAILURE;
            }

            User user = userDAO.getOne(username);
            user.setPassword(MD5Encrypt.MD5(newPassword));
            userDAO.saveAndFlush(user);
        } catch (DataAccessException |PersistenceException e){
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }

    /**
     *
     * @param username id and name of user
     * @return the details of User
     */
    @RequestMapping("/getUserInfo")
    private UserInfo getUserInfo(@RequestParam String username){
        try {
            return userDAO.getOne(username).getUserInfo();
        } catch (PersistenceException e){
            return null;
        }
    }

    /**
     *
     * @param userInfo edited user information
     * @param username id and name
     * @return resultMessage
     */
    @RequestMapping("/updateUserInfo")
    private ResultMessage updateUserInfo (@RequestParam UserInfo userInfo, @RequestParam String username){
        try {
            User user = userDAO.getOne(username);
            user.setUserInfo(userInfo);
            userDAO.saveAndFlush(user);
        } catch (DataAccessException |PersistenceException e){
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }
}
