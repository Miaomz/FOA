package org.foa.data.optiondata;

import org.foa.util.ResultMessage;

/**
 * @author 王川源
 */
public interface OptionCustom {

    ResultMessage addInterestedOption(String optionCode, String userId);

    ResultMessage deleteInterestedOption(String optionCode, String userId);

}
