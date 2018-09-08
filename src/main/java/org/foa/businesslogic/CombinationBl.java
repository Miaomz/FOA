package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.data.combinationdata.CombinationDAO;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.*;
import org.foa.util.ArbitrageUtil;
import org.foa.util.ResultMessage;
import org.foa.vo.CombinationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private CombinationVO trans(Combination combination){
        CombinationVO combinationVO = new CombinationVO(combination);
        combinationVO.setOptUp1(optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp1()));
        combinationVO.setOptUp2(optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp2()));
        combinationVO.setOptDown1(optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown1()));
        combinationVO.setOptDown2(optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown2()));
        return combinationVO;
    }

    private Evaluation evaluate(Combination combination) {
        Option optUp1 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp1());
        Option optUp2 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptUp2());
        Option optDown1 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown1());
        Option optDown2 = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(combination.getOptDown2());
        return ArbitrageUtil.calculateEvaluation(optUp1, optDown1, optUp2, optDown2);
    }

    /**
     * 添加某一期权组合
     * @param combinationJson 例：{userId: xx, optUp1: 50ETF购9月2750, optDown1: 50ETF沽9月2750, optUp2: 50ETF购10月2750, optDown2: 50ETF沽10月2750} 注：4个合约的到期日应该为同一天
     * @return
     */
    @RequestMapping("/purchaseCombination")
    @Transactional
    public ResultMessage purchaseCombination(@RequestParam String combinationJson) {
        Combination combination = gson.fromJson(combinationJson, Combination.class);
        combination.setTime(LocalDateTime.now());
        combination.setEvaluation(evaluate(combination));
        Combination entity = combinationDAO.saveAndFlush(combination);
        return entity.getCid() == 0 ? ResultMessage.FAILURE : ResultMessage.SUCCESS; //自增主键id从1开始，若为0则为默认值失败
    }

    /**
     * 得到用户所持有的所有期权组合
     * @param userId 用户Id
     * @return
     */
    @RequestMapping("/getCurrentCombinations")
    public List<CombinationVO> getCurrentCombinations(@RequestParam String userId) {
        List<CombinationVO> res = new ArrayList<>();
        List<Combination> combs =  combinationDAO.findByUserIdOrderByEvaluationDifferenceDesc(userId);
        combs.forEach(combination -> res.add(trans(combination)));
        return res;
    }

    /**
     * 评价某一期权组合
     * @param combinationJson 例：{optUp1: 50ETF购9月2750, optDown1: 50ETF沽9月2750, optUp2: 50ETF购10月2750, optDown2: 50ETF沽10月2750} 注：4个合约的到期日应该为同一天
     * @return 例：{difference: 200} difference为|term1 - term2|
     */
    @RequestMapping("/evaluateCombination")
    public Evaluation evaluateCombination(@RequestParam String combinationJson) {
        Combination combination = gson.fromJson(combinationJson, Combination.class);
        return evaluate(combination);
    }

    /**
     * 得到这一时刻可以得到的所有期权组合，默认按|term1-term2|倒叙排列
     * @return
     */
    @RequestMapping("/getRankedCombinations")
    public List<CombinationVO> getRankedCombinations() {
        List<Option> options = optionDAO.findCurrentOptions();
        List<Combination> combinations = ArbitrageUtil.getOptCombination(options).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<CombinationVO> res = new ArrayList<>();
        combinations.forEach(combination -> res.add(trans(combination)));
        return res;
    }


    /**
     * 1. void purchaseCombination(String userId, Combination combination);
     * Combination需设置四个期权
     * 2. Combination getCurrentCombinations(String userId);
     * 3. Evaluation evaluateCombination(Combination combination);
     * 期权组合的盈利指标暂时仅有Term1-Term2的绝对值
     * 4. List<Combination> getRankedCombinations(); 得到现在所有的期权组合
     */
}
