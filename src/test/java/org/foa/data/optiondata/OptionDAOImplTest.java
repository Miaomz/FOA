package org.foa.data.optiondata;

import org.foa.FoaApp;
import org.foa.entity.Option;
import org.foa.util.ResultMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoaApp.class)
@WebAppConfiguration
@Transactional
public class OptionDAOImplTest {

    @Autowired
    private OptionDAO optionDAO;

    @Test
    @Rollback
    public void findFirstByOptionAbbrOrderByTimeDesc() {
        Option opt1 = new Option();
        opt1.setTime(LocalDateTime.now());
        opt1.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt1);
        Option opt2 = new Option();
        opt2.setTime(LocalDateTime.now().plusDays(1));
        opt2.setOptionAbbr("50ETF购2200");
        opt2.setBidPrice(100);
        optionDAO.saveAndFlush(opt2);
        assertEquals(100, optionDAO.findFirstByOptionAbbrOrderByTimeDesc("50ETF购2200").getBidPrice(), 0);
    }

    @Test
    @Rollback
    public void findCurrentOptions() {
        LocalDateTime time = LocalDateTime.now();
        Option opt1 = new Option();
        opt1.setTime(time);
        opt1.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt1);
        Option opt2 = new Option();
        opt2.setTime(time.plusDays(1));
        opt2.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt2);
        Option opt3 = new Option();
        opt3.setTime(time.plusDays(1));
        opt3.setOptionAbbr("50ETF沽2200");
        optionDAO.saveAndFlush(opt3);
        assertEquals(2, optionDAO.findCurrentOptions().size());
    }

    @Test
    @Rollback
    public void findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc() {
        LocalDateTime time = LocalDateTime.now();
        Option opt1 = new Option();
        opt1.setTime(time);
        opt1.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt1);
        Option opt2 = new Option();
        opt2.setTime(time.plusDays(1));
        opt2.setOptionAbbr("50ETF购2200");
        opt2.setBidPrice(100);
        optionDAO.saveAndFlush(opt2);
        assertEquals(100, optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc("50ETF购2200", time.plusDays(2)).getBidPrice(), 0);
        assertEquals(null, optionDAO.findFirstByOptionAbbrAndTimeBeforeOrderByTimeDesc("50ETF购2200", time.minusDays(2)));
    }

    @Test
    @Rollback
    public void findByOptionAbbr() {
        LocalDateTime time = LocalDateTime.now();
        Option opt1 = new Option();
        opt1.setTime(time);
        opt1.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt1);
        Option opt2 = new Option();
        opt2.setTime(time.plusDays(1));
        opt2.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt2);
        assertEquals(2, optionDAO.findByOptionAbbr("50ETF购2200").size());
    }

    @Test
    @Rollback
    public void findByOptionAbbrAndTimeBefore() {
        LocalDateTime time = LocalDateTime.now();
        Option opt1 = new Option();
        opt1.setTime(time);
        opt1.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt1);
        Option opt2 = new Option();
        opt2.setTime(time.plusDays(1));
        opt2.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt2);
        assertEquals(0, optionDAO.findByOptionAbbrAndTimeBefore("50ETF购2200", time).size());
        assertEquals(1, optionDAO.findByOptionAbbrAndTimeBefore("50ETF购2200", time.plusDays(1)).size());
    }

    @Test
    @Rollback
    public void findInterestedOptions() {
        LocalDateTime time = LocalDateTime.now();
        Option opt1 = new Option();
        opt1.setTime(time);
        opt1.setOptionAbbr("50ETF购2200");
        optionDAO.saveAndFlush(opt1);
        Option opt2 = new Option();
        opt2.setTime(time.plusDays(1));
        opt2.setOptionAbbr("50ETF购2200");
        opt2.setBidPrice(100);
        optionDAO.saveAndFlush(opt2);
        optionDAO.addInterestedOption("50ETF购2200", "wcy");
        assertEquals(100, optionDAO.findInterestedOptions("wcy").get(0).getBidPrice(), 0);
    }

    @Test
    @Rollback
    public void addInterestedOption() {
        assertEquals(ResultMessage.SUCCESS, optionDAO.addInterestedOption("50ETF购2200", "wcy"));
    }

    @Test
    @Rollback
    public void deleteInterestedOption() {
        optionDAO.addInterestedOption("50ETF购2200", "wcy");
        assertEquals(ResultMessage.SUCCESS, optionDAO.deleteInterestedOption("50ETF购2200", "wcy"));
    }
}