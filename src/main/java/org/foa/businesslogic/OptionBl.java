package org.foa.businesslogic;

import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Option;
import org.foa.entity.OptionItem;
import org.foa.entity.OptionType;
import org.foa.entity.User;
import org.foa.util.HttpUtil;
import org.foa.util.ResultMessage;
import org.foa.util.SortDTO;
import org.foa.util.SortUtil;
import org.foa.vo.Quotation;
import org.foa.vo.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/OptionBl")
public class OptionBl {

    @Autowired
    private OptionDAO optionDAO;

    @RequestMapping("/getQuotation")
    public Quotation getQuotation(){
        return HttpUtil.getQuotation();
    }

    /**
     * 获取这一时刻可以获取的所有期权的信息
     * 并组合为同一个行权价的标的物的两个合约的集合
     * @return
     */
    @RequestMapping("/getTargets")
    public List<Target> getTargets() {
        List<Target> targets = new ArrayList<>();
        List<Option> options = optionDAO.findCurrentOptions();
        Map<Double, List<Option>> combs = options.stream().collect(Collectors.groupingBy(Option::getExecPrice));
        for(List<Option> opts : combs.values()){
            assert opts.size() == 2 : "有多个标的物行权价相同";
            Option optUp = opts.get(0).getOptionType() == OptionType.UP ? opts.get(0) : opts.get(1);
            Option optDown = opts.get(0).getOptionType() == OptionType.DOWN ? opts.get(0) : opts.get(1);
            targets.add(new Target(optUp, optDown));
        }
        return targets;
    }

    /**
     * 得到同一个行权价的标的物的两个合约
     * @param tid
     * 代表两个合约的共同简称
     * 如50ETF购9月2200，50ETF沽9月2200 则为50ETF9月2200 方便数据层查找
     * @return
     */
    @RequestMapping("/getTarget")
    public Target getTarget(@RequestParam String tid){
        String prefix = "50ETF";
        String suffix = tid.substring(5);
        Option optUp = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(prefix + "购" + suffix);
        Option optDown = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(prefix + "沽" + suffix);
        return new Target(optUp, optDown);
    }

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

    /**
     * 合约的排行榜
     * @param dayNum 统计天数 这个天数有点疑问，谁要的这个接口到时再讨论
     * @param upperLimit 涨跌幅上限
     * @param lowerLimit 涨跌幅下限
     * @param sortType 排序依据，热度（成交量)volume，涨跌幅绝对值quoteChange 还有一个套利可能性不知道怎么判断
     * @return OptionItem 包括排名ranking，期权合约Option
     */
    @RequestMapping("/getRanking")
    public List<OptionItem> getRanking(@RequestParam int dayNum, @RequestParam float upperLimit, @RequestParam float lowerLimit, @RequestParam String sortType) {
        List<Option> optsSorted = optionDAO.findCurrentOptions(SortUtil.sortBy(new SortDTO(sortType))); //排序
        List<Option> optsAfterFilter = optsSorted.stream().filter(option -> option.getQuoteChange() >= lowerLimit && option.getQuoteChange() <= upperLimit).collect(Collectors.toList()); //筛选
        List<OptionItem> res = new ArrayList<>();
        long rank = 0;
        for (Option opt : optsAfterFilter){
            res.add(new OptionItem(rank++, opt));
        }
        return res;
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