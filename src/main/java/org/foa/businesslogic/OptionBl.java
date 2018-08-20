package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Option;
import org.foa.entity.OptionItem;
import org.foa.entity.OptionType;
import org.foa.entity.User;
import org.foa.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/OptionBl")
public class OptionBl {

    private Gson gson = new Gson();

    @Autowired
    private OptionDAO optionDAO;

    @RequestMapping("/getOption")
    public Option getOption(@RequestParam String optionAbbr) {
        return optionDAO.findFirstByOptionAbbrOrderByTimeDesc(optionAbbr);
    }


    @RequestMapping("/getCategory")
    public List<Option> getCategory(@RequestParam OptionType optionType) {
        return optionDAO.findByOptionType(optionType);
    }

    @RequestMapping("/getRanking")
    public List<OptionItem> getRanking(@RequestParam int dayNum, @RequestParam float upperLimit, @RequestParam float lowerLimit, @RequestParam String sortType) {
        //TODO
        return null;
    }

    @RequestMapping("/addInterestedOption")
    @Transactional
    public ResultMessage addInterestedOption(@RequestParam String optionAbbr, @RequestParam String userId) {
        return optionDAO.addInterestedOption(optionAbbr, userId);
    }

    @RequestMapping("/deleteInterestedOption")
    @Transactional
    public ResultMessage deleteInterestedOption(@RequestParam String optionAbbr, @RequestParam String userId) {
        return optionDAO.deleteInterestedOption(optionAbbr, userId);
    }


    @RequestMapping("/findInterestedOptions")
    public List<Option> findInterestedOptions(@RequestParam String userId) {
        return optionDAO.findInterestedOptions(userId);
    }

    @RequestMapping("/findInterestingUsers")
    public List<User> findInterestingUsers(@RequestParam String optionAbbr) {
        return optionDAO.findInterestingUsers(optionAbbr);
    }

    /**
     * 1. Option getOption(String optionAbbr)
     * 2. List<Option> getCategory(OptionType optionType)
     * 3. List<OptionItem> getRanking
     * 4. ResultMessage addInterestedOption(String optionAbbr, User user);//添加至期权池
     * 5. ResultMessage deleteInterestedOption(String optionAbbr, User user);//从期权池中删除
     * 6. List<Option> findInterestedOptions(User user);
     * 7. List<User> findInterestingUsers(Option option);
     */

    /**
     * Make singleton.
     * Usage Instance: OptionBl.optionBl().functionName();
     */
//    private OptionBl() {
//    }
//
//    ;
//    private OptionBl optionBl;
//
//    public OptionBl optionBl() {
//        if (optionBl == null) {
//            optionBl = new OptionBl();
//        }
//        return optionBl;
//    }
}
