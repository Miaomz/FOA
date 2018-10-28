package org.foa;

import org.foa.businesslogic.CombinationBl;
import org.foa.businesslogic.OptionBl;
import org.foa.businesslogic.TransactionBl;
import org.foa.businesslogic.UserBl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
@Transactional
public class FoaAppTest {

    @Autowired
    private UserBl userBl;

    @Autowired
    private OptionBl optionBl;

    @Autowired
    private CombinationBl combinationBl;

    @Autowired
    private TransactionBl transactionBl;

    @Test
    @Rollback(false)
    public void test(){
    }
}