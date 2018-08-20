package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.data.combinationdata.CombinationDAO;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.*;
import org.foa.util.ArbitrageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/CombinationBl")
public class CombinationBl {

    private Gson gson = new Gson();

    @Autowired
    private CombinationDAO combinationDAO;

    @Autowired
    private OptionDAO optionDAO;

    @RequestMapping("/setCurrentCombination")
    private String setCurrentCombination2(@RequestParam String userId, @RequestParam String combinationJson) {
        Combination combination = gson.fromJson(combinationJson, Combination.class);
        //TODO
        return null;
    }

    @RequestMapping("/getCurrentCombination")
    private Combination getCurrentCombination(@RequestParam String userId) {
        //TODO
        return null;
    }

    @RequestMapping("/evaluateCombination")
    private Evaluation evaluateCombination(@RequestParam String combinationJson) {
        Combination combination = gson.fromJson(combinationJson, Combination.class);
        Option optUp1 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp1());
        Option optUp2 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp2());
        Option optDown1 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown1());
        Option optDown2 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown2());
        return ArbitrageUtil.calculateEvaluation(optUp1, optDown1, optUp2, optDown2);
    }

    @RequestMapping("/getRankedCombinations")
    private List<Combination> getRankedCombinations() {
        List<Option> options = optionDAO.findCurrentOptions();
        List<Combination> combinations = ArbitrageUtil.getOptCombination(options);
        return combinations.stream().sorted().collect(Collectors.toList());
    }


    /**
     * 1. ResultMessage setCurrentCombination(User user, Combination combination);
     * 2. Combination getCurrentCombination(User user);//每⼀个⽤户仅能有⼀个当前期权组合
     * Combination包括两项Option，及其购买数量（默认购买⽐例为1:1:1:1，故只需⼀个int），盈利指
     * 标Evaluation
     * 3. ResultMessage addInterestedOption(Option option, User user);//InterestedOption对应“我的期权”
     * 4. ResultMessage deleteInterestedOption(Option option, User user);
     * 5. ResultMessage modifyInterestedOption(Option option, User user);
     * 6. List<Option> findInterestedOptions(User user);
     * 7. List<User> findInterestingUsers(Option option);
     * 8. Evaluation evaluateCombination(Combination combination);
     * 期权组合的盈利指标暂时仅有Term1-Term2的绝对值
     * 9. List<Combination> getRankedCombinations(List<RankTerm> terms);
     * RankTerm为接⼝，暂时仅有DefaultRank（按盈利指标降序）⼀种实现
     */


    /**
     * Make singleton
     * Usage Instance: CombinationBl.combinationBl().functionName();
     */
    private CombinationBl() {
    }

    ;
    private CombinationBl combinationBl;

    public CombinationBl combinationBl() {
        if (combinationBl == null) {
            combinationBl = new CombinationBl();
        }
        return combinationBl;
    }
}
