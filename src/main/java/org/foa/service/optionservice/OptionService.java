package org.foa.service.optionservice;

import org.foa.entity.Option;
import org.foa.entity.OptionItem;
import org.foa.entity.OptionType;
import org.foa.entity.User;
import org.foa.util.ResultMessage;
import org.foa.vo.Quotation;
import org.foa.vo.Target;

import java.util.List;

public interface OptionService {

    Quotation getQuotation();

    List<Target> getTargets();

    Target getTarget(String tid);

    List<Option> getOptions();

    Option getOption(String optionAbbr);

    List<Option> getCategory(OptionType optionType);

    List<OptionItem> getRanking(int dayNum, float upperLimit, float lowerLimit, String sortType);

    ResultMessage addInterestedOption(String optionAbbr, String userId);

    ResultMessage deleteInterestedOption(String optionAbbr, String userId);

    List<Option> findInterestedOptions(String userId);

    List<User> findInterestingUsers(String optionAbbr);
}
