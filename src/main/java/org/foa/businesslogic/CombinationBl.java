package org.foa.businesslogic;

import org.foa.data.combinationdata.CombinationDAO;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.*;
import org.foa.util.ArbitrageUtil;
import org.foa.util.ResultMessage;
import org.foa.vo.CombinationVO;
import org.foa.vo.GraphOfTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/CombinationBl")
public class CombinationBl {

    @Autowired
    private CombinationDAO combinationDAO;

    @Autowired
    private OptionDAO optionDAO;

    @Autowired
    private TransactionBl transactionBl;

    private CombinationVO trans(Combination combination) {
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
     * 期权组合的交易 开仓与平仓时做相反操作
     * 期权组合中四个期权的交易方向（买卖）根据是否是多头（term1>=term2）决定
     * @param combination
     * @param type
     * @param bear
     */
    private void trade(Combination combination, TransactionType type, boolean bear) {
        if ((bear && type == TransactionType.OPEN) || (!bear && type == TransactionType.CLOSE)) {
            transactionBl.purchaseOption(combination.getOptUp1(), type, TransactionDirection.SELL, combination.getPurchaseNum(), combination.getUserId());
            transactionBl.purchaseOption(combination.getOptDown1(), type, TransactionDirection.BUY, combination.getPurchaseNum(), combination.getUserId());
            transactionBl.purchaseOption(combination.getOptUp2(), type, TransactionDirection.BUY, combination.getPurchaseNum(), combination.getUserId());
            transactionBl.purchaseOption(combination.getOptDown2(), type, TransactionDirection.SELL, combination.getPurchaseNum(), combination.getUserId());
        } else if ((bear && type == TransactionType.CLOSE) || (!bear && type == TransactionType.OPEN)) {
            transactionBl.purchaseOption(combination.getOptUp1(), type, TransactionDirection.BUY, combination.getPurchaseNum(), combination.getUserId());
            transactionBl.purchaseOption(combination.getOptDown1(), type, TransactionDirection.SELL, combination.getPurchaseNum(), combination.getUserId());
            transactionBl.purchaseOption(combination.getOptUp2(), type, TransactionDirection.SELL, combination.getPurchaseNum(), combination.getUserId());
            transactionBl.purchaseOption(combination.getOptDown2(), type, TransactionDirection.BUY, combination.getPurchaseNum(), combination.getUserId());
        }
    }

    /**
     * 购买一个期权组合
     *
     * @param userId
     * @param optUp1
     * @param optDown1
     * @param optUp2
     * @param optDown2 注 四个合约到期日应该是同一天
     * @return
     */
    @RequestMapping("/purchaseCombination")
    @Transactional
    public ResultMessage purchaseCombination(@RequestParam String userId, @RequestParam String optUp1, @RequestParam String optDown1, @RequestParam String optUp2, @RequestParam String optDown2) {
        Combination combination = new Combination(optUp1, optDown1, optUp2, optDown2);
        combination.setUserId(userId);
        combination.setTime(LocalDateTime.now());
        Evaluation eva = evaluate(combination);
        combination.setEvaluation(eva);
        combination.setState(CombinationState.PURCHASED);
        //开仓
        trade(combination, TransactionType.OPEN, eva.getTerm1() >= eva.getTerm2());
        return combinationDAO.saveAndFlush(combination).getCid() == 0 ? ResultMessage.FAILURE : ResultMessage.SUCCESS;
    }

    /**
     * 在到期日时卖出持有的期权组合
     *
     * @param cid 用户已持有的期权组合的id
     * @return
     */
    @RequestMapping("/sellCombination")
    @Transactional
    public ResultMessage sellCombination(@RequestParam Long cid) {
        try {
            Combination combination = combinationDAO.getOne(cid);
            combination.setState(CombinationState.SOLD);
            combinationDAO.saveAndFlush(combination);
            //平仓
            trade(combination, TransactionType.CLOSE, combination.getEvaluation().getTerm1() >= combination.getEvaluation().getTerm2());
        } catch (DataAccessException | PersistenceException e) {
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }

    /**
     * 收藏某一期权组合
     *
     * @param userId
     * @param optUp1
     * @param optDown1
     * @param optUp2
     * @param optDown2 注 四个合约的到期日应为同一天
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
     *
     * @param userId 用户Id
     * @param state  INTERESTED 收藏的 PURCHASED 购入的(持有的)
     * @return
     */
    @RequestMapping("/getCombinationsByState")
    public List<CombinationVO> getCurrentCombinations(@RequestParam String userId, @RequestParam CombinationState state) {
        List<CombinationVO> res = new ArrayList<>();
        List<Combination> combs = combinationDAO.findByUserIdAndStateOrderByEvaluationDifferenceDesc(userId, state);
        combs.forEach(combination -> res.add(trans(combination)));
        return res;
    }

    /**
     * 评价某一期权组合
     *
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
     *
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
    public Combination findCombinationById(@RequestParam long cid) {
        return combinationDAO.getOne(cid);
    }

    /**
     * 期权组合的今日价差图
     * @param optUp1
     * @param optDown1
     * @param optUp2
     * @param optDown2
     * @return <LocalTime, Double>
     */
    @RequestMapping("/drawDifference")
    public List<GraphOfTime<LocalTime>> drawDifference(@RequestParam String optUp1, @RequestParam String optDown1, @RequestParam String optUp2, @RequestParam String optDown2){
        List<GraphOfTime<LocalTime>> res = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59);
        List<Option> optUp1s = optionDAO.findByOptionAbbrAndTimeAfterAndTimeBeforeOrderByTimeAsc(optUp1, startTime, endTime);
        List<Option> optDown1s = optionDAO.findByOptionAbbrAndTimeAfterAndTimeBeforeOrderByTimeAsc(optDown1, startTime, endTime);
        List<Option> optUp2s = optionDAO.findByOptionAbbrAndTimeAfterAndTimeBeforeOrderByTimeAsc(optUp2, startTime, endTime);
        List<Option> optDown2s = optionDAO.findByOptionAbbrAndTimeAfterAndTimeBeforeOrderByTimeAsc(optDown2, startTime, endTime);
        for (int i = 0; i < optUp1s.size(); i++){
            res.add(new GraphOfTime<>(LocalTime.of(optUp1s.get(i).getTime().getHour(), optUp1s.get(i).getTime().getMinute()),
                    ArbitrageUtil.calculateEvaluation(optUp1s.get(i), optDown1s.get(i), optUp2s.get(i), optDown2s.get(i)).getDifference()));
        }
        return res;
    }
}
