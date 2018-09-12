package org.foa.businesslogic;

import org.foa.data.combinationdata.CombinationDAO;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Combination;
import org.foa.entity.CombinationState;
import org.foa.entity.Evaluation;
import org.foa.entity.Option;
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
     * 购买一个期权组合
     * @param userId
     * @param optUp1
     * @param optDown1
     * @param optUp2
     * @param optDown2
     * 注 四个合约到期日应该是同一天
     * @return
     */
    @RequestMapping("/purchaseCombination")
    @Transactional
    public ResultMessage purchaseCombination(@RequestParam String userId, @RequestParam String optUp1, @RequestParam String optDown1, @RequestParam String optUp2, @RequestParam String optDown2) {
        return ResultMessage.SUCCESS;
    }

    /**
     * 收藏某一期权组合
     * @param userId
     * @param optUp1
     * @param optDown1
     * @param optUp2
     * @param optDown2
     * 注 四个合约的到期日应为同一天
     * @return
     */
    @RequestMapping("/addInterestedCombination")
    public ResultMessage addInterestedCombination(@RequestParam String userId, @RequestParam String optUp1, @RequestParam String optDown1, @RequestParam String optUp2, @RequestParam String optDown2) {
        Combination combination = new Combination(optUp1, optDown1, optUp2, optDown2);
        combination.setUserId(userId);
        combination.setTime(LocalDateTime.now());
        combination.setEvaluation(evaluate(combination));
        combination.setState(CombinationState.INTERESTED);
        Combination entity = combinationDAO.saveAndFlush(combination);
        return entity.getCid() == 0 ? ResultMessage.FAILURE : ResultMessage.SUCCESS; //自增主键id从1开始，若为0则为默认值失败
    }

    /**
     * 根据状态得到用户的期权组合
     * @param userId 用户Id
     * @param state INTERESTED 收藏的 PURCHASED 购入的(持有的)
     * @return
     */
    @RequestMapping("/getCombinationsByState")
    public List<CombinationVO> getCurrentCombinations(@RequestParam String userId, @RequestParam CombinationState state) {
        List<CombinationVO> res = new ArrayList<>();
        List<Combination> combs =  combinationDAO.findByUserIdAndStateOrderByEvaluationDifferenceDesc(userId, state);
        combs.forEach(combination -> res.add(trans(combination)));
        return res;
    }

    /**
     * 评价某一期权组合
     * @param optUp1
     * @param optDown1
     * @param optUp2
     * @param optDown2
     * @return 例：{difference: 200} difference为|term1 - term2|
     */
    @RequestMapping("/evaluateCombination")
    public Evaluation evaluateCombination(@RequestParam String optUp1, @RequestParam String optDown1, @RequestParam String optUp2, @RequestParam String optDown2) {
        Combination combination = new Combination(optUp1, optDown1, optUp2, optDown2);
        return evaluate(combination);
    }

    /**
     * 得到这一时刻可以得到的所有期权组合，默认按|term1-term2|倒叙排列
     * @return
     */
    @RequestMapping("/getRankedCombinations")
    public List<CombinationVO> getRankedCombinations() {
        List<Option> options = optionDAO.findCurrentOptions();
        List<CombinationVO> combinations = ArbitrageUtil.getOptCombination(options).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return combinations;
    }

    /**
     * find by id
     * @param cid id of combination
     * @return specific combination
     */
    @RequestMapping("/findCombinationById")
    public Combination findCombinationById(@RequestParam long cid){
        return combinationDAO.getOne(cid);
    }
}
