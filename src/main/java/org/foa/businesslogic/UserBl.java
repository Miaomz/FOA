package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.entity.User;
import org.foa.entity.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/UserBl")
public class UserBl {
    //private Gson gson= new Gson();


    @RequestMapping("/login")
    private String login(@RequestParam String username, @RequestParam String password) {
        //TODO
        return null;
    }

    @RequestMapping("/signUp")
    private String signUp(@RequestParam String username ,@RequestParam String  firstPassword, @RequestParam String repetitiousPassword){
        //TODO
        return null;
    }

    @RequestMapping("/modifyPassword")
    private String modifyPassword(@RequestParam String username ,@RequestParam String originalPassword, @RequestParam String newPassword){
        //TODO
        return null;
    }

    @RequestMapping("/getUserInfo")
    private UserInfo getUserInfo(@RequestParam String username){
        return null;
    }


    @RequestMapping("/updateUserInfo")
    private String updateUserInfo (@RequestParam UserInfo userInfo){
        //TODO
        return null;
    }

    /**
     * Make singleton
     * Usage Instance: UserBl.userBl().functionName();
     */
    private UserBl(){};
    private UserBl userBl;
    public UserBl userBl(){
        if (userBl==null){
            userBl=new UserBl();
        }
        return userBl;
    }

}
