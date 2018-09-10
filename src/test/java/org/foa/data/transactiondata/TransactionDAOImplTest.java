package org.foa.data.transactiondata;

import org.foa.FoaApp;
import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Option;
import org.foa.entity.Transaction;
import org.foa.entity.TransactionDirection;
import org.foa.entity.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
@Transactional
public class TransactionDAOImplTest {

    @Autowired
    private OptionDAO optionDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Test
    @Rollback(false)
    public void addTransactions(){
        Random ra = new Random();
        for(int i = 0; i <= 99; i++){
            Transaction t = new Transaction();
            t.setUserId("Default");
            long optId = ra.nextInt(80) + 1;
            Option option = optionDAO.getOne(optId);
            t.setOptionAbbr(option.getOptionAbbr());
            t.setPrice(option.getLatestPrice());
            t.setTime(option.getTime());
            int q = ra.nextInt(100);
            t.setQuantity(q);
            int t1 = ra.nextInt(2);
            int t2 = ra.nextInt(2);
            t.setTransactionDirection(TransactionDirection.values()[t1]);
            t.setTransactionType(TransactionType.values()[t2]);
            transactionDAO.saveAndFlush(t);
        }
    }

    @Test
    @Rollback
    public void test(){
        System.out.println(transactionDAO.findAll().size());
    }

    @Test
    @Rollback
    public void findAllByOrderByTimeDesc() {
        Transaction t1 = new Transaction();
        t1.setTime(LocalDateTime.now());
        transactionDAO.saveAndFlush(t1);
        Transaction t2 = new Transaction();
        t2.setTime(LocalDateTime.now().plusDays(1));
        transactionDAO.saveAndFlush(t2);
        List<Transaction> arr = transactionDAO.findAllByOrderByTimeDesc();
        assertEquals(true, arr.get(0).getTime().isAfter(arr.get(1).getTime()));
    }

    @Test
    @Rollback
    public void findAllByOrderByTimeAsc() {
        Transaction t1 = new Transaction();
        t1.setTime(LocalDateTime.now());
        transactionDAO.saveAndFlush(t1);
        Transaction t2 = new Transaction();
        t2.setTime(LocalDateTime.now().plusDays(1));
        transactionDAO.saveAndFlush(t2);
        List<Transaction> arr = transactionDAO.findAllByOrderByTimeAsc();
        assertEquals(true, arr.get(0).getTime().isBefore(arr.get(1).getTime()));
    }

    @Test
    @Rollback
    public void findByUserIdOrderByTimeDesc() {
        Transaction t1 = new Transaction();
        t1.setUserId("wcy");
        transactionDAO.saveAndFlush(t1);
        Transaction t2 = new Transaction();
        t2.setUserId("wcy");
        transactionDAO.saveAndFlush(t2);
        assertEquals(2, transactionDAO.findByUserIdOrderByTimeDesc("wcy").size());
    }
}