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
     * 每三分钟获取一次数据
     */
    @Scheduled(cron = "0 */3 * * * ?")
    public void saveDataFromSina(){
        List<Option> options = HttpUtil.recordAllOptions();
        optionDAO.saveAll(options);
    }
}
