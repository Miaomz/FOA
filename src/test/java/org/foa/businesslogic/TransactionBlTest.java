package org.foa.businesslogic;

import org.foa.FoaApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author miaomuzhi
 * @since 2018/10/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
public class TransactionBlTest {

    @Autowired
    private TransactionBl transactionBl;

    @Test
    public void drawReturnRate() {
        System.out.println(transactionBl.drawReturnRate("foo"));
    }
}