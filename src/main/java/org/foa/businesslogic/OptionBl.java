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

    /**
     * 获取这一时刻可以获取的所有期权的信息
     * @return
     */
    @RequestMapping("/getOptions")
    public List<Option> getOptions() {
        return optionDAO.findCurrentOptions();
    }

    /**
     * 获取期权的信息
     * @param optionAbbr 期权合约简称 例：50ETF购9月2750
     * @return
     */
    @RequestMapping("/getOption")
    public Option getOption(@RequestParam String optionAbbr) {
        return optionDAO.findFirstByOptionAbbrOrderByTimeDesc(optionAbbr);
    }

    /**
     * 根据分类获得该分类的所有期权，因目前只有50ETF，该方法暂无实际意义
     * @param optionType OptionType.UP, OptionType.DOWN
     * @return
     */
    @RequestMapping("/getCategory")
    public List<Option> getCategory(@RequestParam OptionType optionType) {
        return optionDAO.findByOptionType(optionType);
    }

    @RequestMapping("/getRanking")
    public List<OptionItem> getRanking(@RequestParam int dayNum, @RequestParam float upperLimit, @RequestParam float lowerLimit, @RequestParam String sortType) {
        //TODO
        return null;
    }

    /**
     * 添加一个期权至我的期权池
     * @param optionAbbr 期权合约简称
     * @param userId 用户Id
     * @return
     */
    @RequestMapping("/addInterestedOption")
    @Transactional
    public ResultMessage addInterestedOption(@RequestParam String optionAbbr, @RequestParam String userId) {
        return optionDAO.addInterestedOption(optionAbbr, userId);
    }

    /**
     * 从我的期权池中删除一个期权
     * @param optionAbbr 期权合约简称
     * @param userId 用户Id
     * @return
     */
    @RequestMapping("/deleteInterestedOption")
    @Transactional
    public ResultMessage deleteInterestedOption(@RequestParam String optionAbbr, @RequestParam String userId) {
        return optionDAO.deleteInterestedOption(optionAbbr, userId);
    }


    /**
     * 获得我的期权池中的所有期权
     * @param userId 用户Id
     * @return
     */
    @RequestMapping("/findInterestedOptions")
    public List<Option> findInterestedOptions(@RequestParam String userId) {
        return optionDAO.findInterestedOptions(userId);
    }

    /**
     * 得到期权池中拥有某一期权的所有用户
     * @param optionAbbr 期权合约简称
     * @return
     */
    @RequestMapping("/findInterestingUsers")
    public List<User> findInterestingUsers(@RequestParam String optionAbbr) {
        return optionDAO.findInterestingUsers(optionAbbr);
    }

    /**
     * 0. List<Option> getOptions()
     * 1. Option getOption(String optionAbbr)
     * 2. List<Option> getCategory(OptionType optionType)
     * 3. List<OptionItem> getRanking
     * 4. ResultMessage addInterestedOption(String optionAbbr, User user);//添加至期权池
     * 5. ResultMessage deleteInterestedOption(String optionAbbr, User user);//从期权池中删除
     * 6. List<Option> findInterestedOptions(User user);
     * 7. List<User> findInterestingUsers(Option option);
     */
}
