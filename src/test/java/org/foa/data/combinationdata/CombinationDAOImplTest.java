package org.foa.data.combinationdata;

import org.foa.FoaApp;
import org.foa.entity.Combination;
import org.foa.entity.CombinationState;
import org.foa.entity.Evaluation;
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

/**
 * @author miaomuzhi
 * @since 2018/9/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
@Transactional
public class CombinationDAOImplTest {
    @Autowired
    private CombinationDAO combinationDAO;

    @Test
    @Rollback
    public void findByUserIdOrderByEvaluationDifferenceDescTest(){
        Combination combination1 = new Combination();
        combination1.setUserId("fsadgklasgj");
        combination1.setOptUp1("UP1");
        combination1.setOptUp2("UP2");
        combination1.setOptDown1("DOWN1");
        combination1.setOptDown2("DOWN2");
        combination1.setTime(LocalDateTime.now());
        combination1.setEvaluation(new Evaluation(200, 100, 100, 100));

        Combination combination2 = new Combination();
        combination2.setUserId("fsadgklasgj");
        combination2.setOptUp1("UP1");
        combination2.setOptUp2("up2");
        combination2.setOptDown1("DOWN1");
        combination2.setOptDown2("DOWN2");
        combination2.setTime(LocalDateTime.now());
        combination2.setEvaluation(new Evaluation(200, 90, 110, 110));
        combinationDAO.saveAndFlush(combination1);
        combinationDAO.saveAndFlush(combination2);
        List<Combination> combinationList = combinationDAO.findByUserIdOrderByEvaluationDifferenceDesc("fsadgklasgj");
        assertEquals(2, combinationList.size());
        assertEquals("up2", combinationList.get(0).getOptUp2());
    }

    @Test
    @Rollback
    public void findByUserIdAndStateOrderByEvaluationDifferenceDescTest(){
        Combination combination = new Combination();
        combination.setState(CombinationState.PURCHASED);
        combination.setUserId("fsdasfd");
        combination.setOptUp1("UP1");
        combination.setOptUp2("UP2");
        combination.setOptDown1("DOWN1");
        combination.setOptDown2("DOWN2");
        combination.setTime(LocalDateTime.now());
        combination.setEvaluation(new Evaluation(200, 100, 100, 100));
        combinationDAO.saveAndFlush(combination);
        assertEquals(1, combinationDAO
                .findByUserIdAndStateOrderByEvaluationDifferenceDesc("fsdasfd", CombinationState.PURCHASED)
                .size());
    }
}