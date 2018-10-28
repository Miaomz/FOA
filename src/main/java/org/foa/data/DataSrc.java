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
 * 50ETF开市时间为早上9点半至11点半，下午1点至3点
 */
@Component
public class DataSrc {

    @Autowired
    private OptionDAO optionDAO;

    /**
     * 从早上九点半至10点内，每三分钟获取一次数据
     */
    @Scheduled(cron = "0 30/3 9 * * ?")
    public void saveDataFromSinaTask1(){
        List<Option> options = HttpUtil.recordAllOptions();
        optionDAO.saveAll(options);
    }

    /**
     * 从早上10点至下午三点内，每三分钟获取一次数据
     */
    @Scheduled(cron = "0 0/3 10-14 * * ?")
    public void saveDataFromSinaTask2(){
        List<Option> options = HttpUtil.recordAllOptions();
        optionDAO.saveAll(options);
    }
}
