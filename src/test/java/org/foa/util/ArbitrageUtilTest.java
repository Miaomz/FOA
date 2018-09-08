package org.foa.util;

import org.foa.FoaApp;
import org.foa.businesslogic.CombinationBl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
@Transactional
public class ArbitrageUtilTest {

    @Autowired
    private CombinationBl combinationBl;

    @Test
    public void getOptCombination() {
        long startTime = System.currentTimeMillis();
        combinationBl.getRankedCombinations();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}