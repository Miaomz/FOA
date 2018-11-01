package org.foa.businesslogic;

import org.foa.data.combinationdata.CombinationDAO;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Combination;
import org.foa.entity.CombinationState;
import org.foa.entity.Evaluation;
import org.foa.entity.TransactionType;
import org.foa.util.ArbitrageUtil;
import org.foa.vo.CombinationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author miaomuzhi
 * @since 2018/10/26
 */
@Component
public class AutoPurchase {

    @Autowired
    private CombinationBl combinationBl;

    @Autowired
    private OptionDAO optionDAO;

    @Autowired
    private CombinationDAO combinationDAO;

    private static int threshold = 4;

    public static void setThreshold(int threshold) {
        AutoPurchase.threshold = threshold;
    }

    public void generateTransactionsRecord(){
        LocalDateTime origin = LocalDateTime.now().minusMonths(3);
        LocalDateTime endDay = LocalDateTime.now();
        for (LocalDateTime endDate = origin.plusDays(1); endDate.isBefore(endDay); endDate = endDate.plusDays(1)){
            List<CombinationVO> combinationVOS = ArbitrageUtil.getOptCombination(optionDAO.findPastOptions(endDate))
                    .stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            if (!combinationVOS.isEmpty()){
                combinationVOS = combinationVOS.subList(0, 1);
            }

            for (CombinationVO combinationVO : combinationVOS) {
                int indicator = calcPotentialProfits(combinationVO, origin, endDate);
                if (indicator != 0){
                    Combination combination = combinationVO.toCombination();
                    combination.setTime(endDate);
                    combination.setUserId("Logan");
                    combination.setState(CombinationState.PURCHASED);
                    combinationBl.trade(combination, TransactionType.OPEN, indicator==1, endDate);
                    combinationDAO.saveAndFlush(combination);
                }
            }

            List<Combination> combinations2dAgo = combinationDAO.findByTimeBeforeOrderByTimeDesc(endDate.minusDays(2));
            List<Combination> combinations1dAgo = combinationDAO.findByTimeBeforeOrderByTimeDesc(endDate.minusDays(1));
            for (Combination combination : combinations1dAgo) {//close these 1 day ago if they should be closed
                if (isToClose(combination, endDate.minusDays(1).toLocalDate())){
                    combination.setTime(endDate);
                    combination.setUserId("Logan");
                    combination.setState(CombinationState.SOLD);
                    combinationBl.trade(combination, TransactionType.CLOSE,
                            combination.getEvaluation().getTerm1()>=combination.getEvaluation().getTerm2(), endDate);
                    combinationDAO.saveAndFlush(combination);
                }
            }
            for (Combination combination : combinations2dAgo) {//close the combination 2 days ago forcefully
                if (combination.getState()==CombinationState.PURCHASED){
                    boolean isSold = false;
                    for (Combination dAgo : combinations1dAgo) {
                        if (dAgo.getState() == CombinationState.SOLD
                                && dAgo.getOptUp1().equals(combination.getOptUp1()) && dAgo.getOptDown1().equals(combination.getOptDown1())
                                && dAgo.getOptUp2().equals(combination.getOptUp2()) && dAgo.getOptDown2().equals(combination.getOptDown2())){
                            isSold = true;
                        }
                    }
                    if (!isSold){
                        combination.setTime(endDate);
                        combination.setUserId("Logan");
                        combination.setState(CombinationState.SOLD);
                        combinationBl.trade(combination, TransactionType.CLOSE,
                                combination.getEvaluation().getTerm1()>=combination.getEvaluation().getTerm2(), endDate);
                        combinationDAO.saveAndFlush(combination);
                    }
                }
            }
            System.err.println("end of loop");
        }
    }

    /**
     *
     * @param combination the options it contains are the latest
     * @param startTime
     * @param endTime
     * @return 0 means that we should not buy it, 1 indicates purchase and -1 indicates sell
     */
    private int calcPotentialProfits(CombinationVO combination, LocalDateTime startTime, LocalDateTime endTime){
        if (combination == null){
            return 0;
        }

        List<Double> differences = new ArrayList<>();
        for (LocalDate date = startTime.toLocalDate(); date.isBefore(endTime.toLocalDate()); date = date.plusDays(1)){
            Evaluation evaluation = ArbitrageUtil.calculateEvaluation(
                    optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptUp1().getOptionAbbr(), date.atTime(18,0)),
                    optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptDown1().getOptionAbbr(), date.atTime(18,0)),
                    optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptUp2().getOptionAbbr(), date.atTime(18,0)),
                    optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptDown2().getOptionAbbr(), date.atTime(18,0)));
            if (evaluation != null){
                differences.add(evaluation.getDifference());
            }
        }

        double mu = 0;
        for (Double difference : differences) {
            mu += difference;
        }
        mu /= differences.size();
        double sigma = 0;
        for (Double difference : differences) {
            sigma += Math.pow(difference-mu, 2);
        }
        sigma = Math.sqrt(sigma/(differences.size()-1));

        int indicator = 0;
        double latestDiff = ArbitrageUtil.calculateEvaluation(combination.getOptUp1(), combination.getOptDown1(), combination.getOptUp2(), combination.getOptDown2()).getDifference();
        if (latestDiff > mu-sigma*threshold){
            indicator = 1;
        } else if (latestDiff < mu-sigma*threshold){
            indicator = -1;
        }
        return indicator;
    }

    private boolean isToClose(Combination combination, LocalDate date){
        double diff = ArbitrageUtil.calculateEvaluation(
                optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptUp1(), date.atTime(18,0)),
                optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptDown1(), date.atTime(18,0)),
                optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptUp2(), date.atTime(18,0)),
                optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc(combination.getOptDown2(), date.atTime(18,0)))
                .getDifference();
        return Math.abs(diff) < 0.01;
    }
}
