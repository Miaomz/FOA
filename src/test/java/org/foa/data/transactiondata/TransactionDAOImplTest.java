package org.foa.data.transactiondata;

import org.foa.FoaApp;
import org.foa.entity.Transaction;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
@Transactional
public class TransactionDAOImplTest {

    @Autowired
    private TransactionDAO transactionDAO;

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