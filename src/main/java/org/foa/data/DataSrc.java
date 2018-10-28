package org.foa.data;

import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Option;
import org.foa.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 王川源
 */
@Component
public class DataSrc {

    @Autowired
    private OptionDAO optionDAO;

    /**
     * 每个星期一至星期五，从早上九点半至下午三点，每三分钟获取一次数据
     */
    @Scheduled(cron = "0 30/3 9-15 0 0 1/5 *")
    public void saveDataFromSina(){
        List<Option> options = HttpUtil.recordAllOptions();
        optionDAO.saveAll(options);
    }
}
