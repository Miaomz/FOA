package org.foa.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tid;

    private LocalDateTime time;

    @OneToMany
    private List<Option> portfolio;

    private double profit;

    public Transaction() {
    }

    public Transaction(long tid, LocalDateTime time, List<Option> portfolio, double profit) {
        this.tid = tid;
        this.time = time;
        this.portfolio = portfolio;
        this.profit = profit;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public List<Option> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<Option> portfolio) {
        this.portfolio = portfolio;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
