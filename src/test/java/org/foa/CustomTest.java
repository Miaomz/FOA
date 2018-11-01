package org.foa;

import org.foa.util.FileUtil;
import org.foa.util.JsonUtil;
import org.foa.vo.GraphOfTime;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void changeFooData(){
        URL url = getClass().getResource("/resp/foo.json");
        if (url != null && new File(url.getFile()).exists()) {
            List<GraphOfTime> graphOfTimes = JsonUtil.toArray(FileUtil.readFile(url.getPath()), GraphOfTime.class);
            List<GraphOfTime<LocalDate>> result = new ArrayList<>(graphOfTimes.size());
            for (GraphOfTime graphOfTime : graphOfTimes) {
                GraphOfTime<LocalDate> point = new GraphOfTime<>(LocalDate.parse((String) graphOfTime.getTime()), graphOfTime.getValue());
                result.add(point);
            }

            for (GraphOfTime<LocalDate> localDateGraphOfTime : result) {
                if (localDateGraphOfTime.getTime().isAfter(LocalDate.parse("2018-09-20"))){
                    localDateGraphOfTime.setValue(localDateGraphOfTime.getValue()+0.02);
                }
            }
            FileUtil.writeFile(getClass().getResource("/resp").getPath() + "/foo.json", JsonUtil.toJson(result));
        }
    }

}
