package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.data.transactiondata.TransactionDAO;
import org.foa.entity.Transaction;
import org.foa.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/TransactionBl")
public class TransactionBl {
    private Gson gson = new Gson();

    @Autowired
    private TransactionDAO transactionDAO;
    /**
     * 1. ResultMessage addTransaction(Transaction t);
     * 2. ResultMessage deleteTransaction(long tid);
     * 3. ResultMessage modifyTransaction(Transaction t);
     * 4. Transaction findTransactionById(long tid);
     * 5. List<Transaction> findAllTransactions(); //默认按照时间倒序排序
     * 6. List<Transaction> findTransactionsByTerm(List<SearchTerm> terms);
     * SearchTerm为接⼝，包括AscTime,DescTime,Period,Profit,Portfolio
     * public interface SearchTerm {
     * List<Transaction> filter(List transactions);
     * }
     * public class Transaction{
     * long tid;
     * LocalDateTime time;
     * List<Option> portfolio;
     * double profit;
     * }
     */

    @RequestMapping("/addTransaction")
    @Transactional
    public ResultMessage addTransaction(@RequestParam String transactionJson){
        Transaction transaction = gson.fromJson(transactionJson, Transaction.class);
        try {
            transactionDAO.saveAndFlush(transaction);
        } catch (DataAccessException|PersistenceException e){
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }

    @RequestMapping("/deleteTransaction")
    @Transactional
    public ResultMessage deleteTransaction(@RequestParam String transactionJson){
        Transaction transaction = gson.fromJson(transactionJson,Transaction.class);
        try {
            transactionDAO.delete(transaction);
        } catch (DataAccessException|PersistenceException e){
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }

    @RequestMapping("/modifyTransaction")
    @Transactional
    public ResultMessage modifyTransaction(@RequestParam String transactionJson){
        Transaction transaction = gson.fromJson(transactionJson,Transaction.class);
        try {
            transactionDAO.saveAndFlush(transaction);
        } catch (DataAccessException|PersistenceException e){
            return ResultMessage.FAILURE;
        }
        return ResultMessage.SUCCESS;
    }
    @RequestMapping("/findTransactionById")
    public Transaction findTransactionById(@RequestParam long tid){
        return transactionDAO.getOne(tid);
    }

    @RequestMapping("/findAllTransactions")
    public List<Transaction> findAllTransactions(){
        return transactionDAO.findAll();
    }

    @RequestMapping("/findTransactionsByTerm")
    public List<Transaction> findTransactionsByTerm(@RequestParam String termsJson){
//        ArrayList<OptionItem> terms = gson.fromJson(termsJson, ArrayList.class);
//        List<Transaction> allTransactions = findAllTransactions();
//        List<Transaction> targetTransactions = new ArrayList<>();
//        allTransactions.forEach(transaction -> {
//            for (String s : transaction.getPortfolio()) {
//                for (OptionItem term : terms) {
//                    if (s.equals(term.getOption().getOptionAbbr())){
//
//                    }
//                }
//            }
//                });
        return null;
    }
}


