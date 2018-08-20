package org.foa.util;

import org.foa.entity.Combination;
import org.foa.entity.Evaluation;
import org.foa.entity.Option;
import org.foa.entity.OptionType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author 王川源
 * 期权四角套利实现
 */
public class ArbitrageUtil {

    /**
     * 组合数
     */
    private static <T> Map<Boolean, List<T>> split(List<T> list, int n) {
        return IntStream
                .range(0, list.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, list.get(i)))
                .collect(partitioningBy(entry -> entry.getKey() < n, mapping(AbstractMap.SimpleEntry::getValue, toList())));
    }

    private static <T> List<List<T>> combinations(List<T> list, int k) {
        if (k == 0 || list.isEmpty()) {//去除K大于list.size的情况。即取出长度不足K时清除此list
            return Collections.emptyList();
        }
        if (k == 1) {//递归调用最后分成的都是1个1个的，从这里面取出元素
            return list.stream().map(e -> Stream.of(e).collect(toList())).collect(toList());
        }
        Map<Boolean, List<T>> headAndTail = split(list, 1);
        List<T> head = headAndTail.get(true);
        List<T> tail = headAndTail.get(false);
        List<List<T>> c1 = combinations(tail, (k - 1)).stream().map(e -> {
            List<T> l = new ArrayList<>();
            l.addAll(head);
            l.addAll(e);
            return l;
        }).collect(Collectors.toList());
        List<List<T>> c2 = combinations(tail, k);
        c1.addAll(c2);
        return c1;
    }

    /**
     * 得到期权组合，两个期权，4个合约
     */
    private static Map<LocalDate, List<Option>> classifyByExpireDay(List<Option> options) {
        Map<LocalDate, List<Option>> res = options.stream().collect(groupingBy(Option::getExpireDay));
        return res;
    }

    private static List<Combination> getCombOfSameExpireDay(List<Option> options) {
        List<Combination> res = new ArrayList<>();
        Map<Double, List<Option>> optsOfSameExecPrice = options.stream().collect(groupingBy(Option::getExecPrice));
        List<Double> execPrices = new ArrayList<>(optsOfSameExecPrice.keySet());
        List<List<Double>> combinations = combinations(execPrices, 2);
        for (List<Double> c : combinations) {
            Option optUp1 = optsOfSameExecPrice.get(c.get(0)).get(0).getOptionType() == OptionType.UP
                    ? optsOfSameExecPrice.get(c.get(0)).get(0) : optsOfSameExecPrice.get(c.get(0)).get(1);

            Option optDown1 = optsOfSameExecPrice.get(c.get(0)).get(0).getOptionType() == OptionType.DOWN
                    ? optsOfSameExecPrice.get(c.get(0)).get(0) : optsOfSameExecPrice.get(c.get(0)).get(1);

            Option optUp2 = optsOfSameExecPrice.get(c.get(1)).get(0).getOptionType() == OptionType.UP
                    ? optsOfSameExecPrice.get(c.get(1)).get(0) : optsOfSameExecPrice.get(c.get(1)).get(1);

            Option optDown2 = optsOfSameExecPrice.get(c.get(1)).get(0).getOptionType() == OptionType.DOWN
                    ? optsOfSameExecPrice.get(c.get(1)).get(0) : optsOfSameExecPrice.get(c.get(1)).get(1);

            Evaluation eva = calculateEvaluation(optUp1, optDown1, optUp2, optDown2);
            Combination comb = new Combination(optUp1.getOptionAbbr(), optDown1.getOptionAbbr(), optUp2.getOptionAbbr(), optDown2.getOptionAbbr(), eva);
            res.add(comb);
        }
        return res;
    }

    public static Evaluation calculateEvaluation(Option optUp1, Option optDown1, Option optUp2, Option optDown2){
        double gamma = 0.0415;  //无风险利率，用16国债19的到期收益率计算
        double tau = (optUp1.getExpireDay().toEpochDay() - LocalDate.now().toEpochDay()) / 360.0;

        double X1 = optUp1.getExecPrice();
        double X2 = optUp2.getExecPrice();
        double C1 = optUp1.getLatestPrice();
        double P1 = optDown1.getLatestPrice();
        double C2 = optUp2.getLatestPrice();
        double P2 = optDown2.getLatestPrice();

        double term1 = P1 - P2;
        double term2 = (C1 - C2) + (X1 - X2) * Math.exp((0 - gamma) * tau);
        Evaluation eva = new Evaluation(Math.abs(term1 - term2));
        return eva;
    }

    /**
     *
     * @param options 同一时刻可获得的同类型的期权如50ETF
     * @return 本组期权可构成的全部组合
     */
    public static List<Combination> getOptCombination(List<Option> options) {
        List<Combination> res = new ArrayList<>();
        Map<LocalDate, List<Option>> optsOfSameExpireDay = classifyByExpireDay(options);
        for (List<Option> opts : optsOfSameExpireDay.values()) {
            res.addAll(getCombOfSameExpireDay(opts));
        }
        return res;
    }

}
