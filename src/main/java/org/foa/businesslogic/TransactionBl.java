package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.entity.OptionItem;
import org.foa.entity.Transaction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/TransactionBl")
public class TransactionBl {
    private Gson gson=new Gson();


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
    private String addTransaction(@RequestParam String transactionJson){
        Transaction transaction=gson.fromJson(transactionJson, Transaction.class);
        //TODO
        return null;
    }

    @RequestMapping("/deleteTransaction")
    private String deleteTransaction(@RequestParam String transactionJson){
        Transaction transaction=gson.fromJson(transactionJson,Transaction.class);
        //TODO
        return null;
    }

    @RequestMapping("/modifyTransaction")
    private String modifyTransaction(@RequestParam String transactionJson){
        Transaction transaction=gson.fromJson(transactionJson,Transaction.class);
        //TODO
        return null;
    }
    @RequestMapping("/findTransactionById")
    private Transaction findTransactionById(@RequestParam long tid){
        //TODO
        return null;
    }



    @RequestMapping("/findAllTransactions")
    private ArrayList<Transaction> findAllTransactions(){
        //TODO
        return null;
    }

    @RequestMapping("/findTransactionsByTerm")
    private ArrayList<Transaction> findTransactionsByTerm(@RequestParam String termsJson){
        ArrayList<OptionItem> terms=gson.fromJson(termsJson,ArrayList.class);
        //TODO
        return null;
    }



    /**
     * Make singleton
     * Usage Instance: TransactionBl.transactionBl().functionName();
     */
    private TransactionBl(){};
    private TransactionBl transactionBl;
    public TransactionBl transactionBl(){
        if (transactionBl==null){
            transactionBl=new TransactionBl();
        }
        return transactionBl;
    }
}


