package org.foa;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
public class CustomTest {

    @Test
    public void test(){
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now();
        date2.minusDays(1);
        assertEquals(date1.toString(), date2.toString());
    }

}
