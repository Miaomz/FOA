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

import java.util.List;

@RestController
@RequestMapping("/OptionBl")
public class OptionBl {

    private Gson gson = new Gson();

    @Autowired
    private OptionDAO optionDAO;

    @RequestMapping("/getOption")
    private Option getOption(@RequestParam String optionAbbr) {
        return optionDAO.findFirstByOptionAbbrOrderByTimeDesc(optionAbbr);
    }


    @RequestMapping("/getCategory")
    private List<Option> getCategory(@RequestParam OptionType optionType) {
        return optionDAO.findByOptionType(optionType);
    }

    @RequestMapping("/getRanking")
    private List<OptionItem> getRanking(@RequestParam int dayNum, @RequestParam float upperLimit, @RequestParam float lowerLimit, @RequestParam String sortType) {
        //TODO
        return null;
    }

    @RequestMapping("/addInterestedOption")
    private ResultMessage addInterestedOption(@RequestParam String optionAbbr, @RequestParam String userId) {
        return optionDAO.addInterestedOption(optionAbbr, userId);
    }

    @RequestMapping("/deleteInterestedOption")
    private ResultMessage deleteInterestedOption(@RequestParam String optionAbbr, @RequestParam String userId) {
        return optionDAO.deleteInterestedOption(optionAbbr, userId);
    }


    @RequestMapping("/findInterestedOptions")
    private List<Option> findInterestedOptions(@RequestParam String userId) {
        return optionDAO.findInterestedOptions(userId);
    }

    @RequestMapping("/findInterestingUsers")
    private List<User> findInterestingUsers(@RequestParam String optionAbbr) {
        return optionDAO.findInterestingUsers(optionAbbr);
    }


    /**
     * Make singleton.
     * Usage Instance: OptionBl.optionBl().functionName();
     */
    private OptionBl() {
    }

    ;
    private OptionBl optionBl;

    public OptionBl optionBl() {
        if (optionBl == null) {
            optionBl = new OptionBl();
        }
        return optionBl;
    }
}
