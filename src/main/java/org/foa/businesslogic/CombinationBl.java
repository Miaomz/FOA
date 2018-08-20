package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.data.combinationdata.CombinationDAO;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.*;
import org.foa.util.ArbitrageUtil;
import org.foa.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
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

    private Evaluation evaluate(Combination combination){
        Option optUp1 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp1());
        Option optUp2 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp2());
        Option optDown1 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown1());
        Option optDown2 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown2());
        return ArbitrageUtil.calculateEvaluation(optUp1, optDown1, optUp2, optDown2);
    }

    @RequestMapping("/purchaseCombination")
    @Transactional
    public void purchaseCombination(@RequestParam String userId, @RequestParam String combinationJson) {
        Combination combination = gson.fromJson(combinationJson, Combination.class);
        combination.setTime(LocalDateTime.now());
        combination.setUserId(userId);
        combination.setEvaluation(evaluate(combination));
        combinationDAO.saveAndFlush(combination);
    }

    @RequestMapping("/getCurrentCombinations")
    public List<Combination> getCurrentCombinations(@RequestParam String userId) {
        return combinationDAO.findByUserIdOrderByEvaluationDifferenceDesc(userId);
    }

    @RequestMapping("/evaluateCombination")
    public Evaluation evaluateCombination(@RequestParam String combinationJson) {
        Combination combination = gson.fromJson(combinationJson, Combination.class);
        return evaluate(combination);
    }

    @RequestMapping("/getRankedCombinations")
    public List<Combination> getRankedCombinations() {
        List<Option> options = optionDAO.findCurrentOptions();
        List<Combination> combinations = ArbitrageUtil.getOptCombination(options);
        return combinations.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }


    /**
     * 1. void purchaseCombination(String userId, Combination combination);
     * Combination需设置四个期权
     * 2. Combination getCurrentCombinations(String userId);
     * 3. Evaluation evaluateCombination(Combination combination);
     * 期权组合的盈利指标暂时仅有Term1-Term2的绝对值
     * 4. List<Combination> getRankedCombinations(); 得到现在所有的期权组合
     */


    /**
     * Make singleton
     * Usage Instance: CombinationBl.combinationBl().functionName();
     */
//    private CombinationBl() {
//    }
//
//    ;
//    private CombinationBl combinationBl;
//
//    public CombinationBl combinationBl() {
//        if (combinationBl == null) {
//            combinationBl = new CombinationBl();
//        }
//        return combinationBl;
//    }
}
