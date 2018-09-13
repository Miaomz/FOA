package org.foa.service.optionservice;

import org.foa.data.optiondata.OptionDAO;
import org.foa.entity.Option;
import org.foa.entity.OptionItem;
import org.foa.entity.OptionType;
import org.foa.entity.User;
import org.foa.util.HttpUtil;
import org.foa.util.ResultMessage;
import org.foa.util.SortDTO;
import org.foa.util.SortUtil;
import org.foa.vo.Quotation;
import org.foa.vo.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionDAO optionDAO;

    @Override
    public Quotation getQuotation() {
        return HttpUtil.getQuotation();
    }

    @Override
    public List<Target> getTargets() {
        List<Target> targets = new ArrayList<>();
        List<Option> options = optionDAO.findCurrentOptions();
        Map<Double, List<Option>> combs = options.stream().collect(Collectors.groupingBy(Option::getExecPrice));
        for(List<Option> opts : combs.values()){
            assert opts.size() == 2 : "有多个标的物行权价相同";
            Option optUp = opts.get(0).getOptionType() == OptionType.UP ? opts.get(0) : opts.get(1);
            Option optDown = opts.get(0).getOptionType() == OptionType.DOWN ? opts.get(0) : opts.get(1);
            targets.add(new Target(optUp, optDown));
        }
        return targets;
    }

    @Override
    public Target getTarget(String tid) {
        String prefix = "50ETF";
        String suffix = tid.substring(5);
        Option optUp = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(prefix + "购" + suffix);
        Option optDown = optionDAO.findFirstByOptionAbbrOrderByTimeDesc(prefix + "沽" + suffix);
        return new Target(optUp, optDown);
    }

    @Override
    public List<Option> getOptions() {
        return optionDAO.findCurrentOptions();
    }

    @Override
    public Option getOption(String optionAbbr) {
        return optionDAO.findFirstByOptionAbbrOrderByTimeDesc(optionAbbr);
    }

    @Override
    public List<Option> getCategory(OptionType optionType) {
        return optionDAO.findByOptionType(optionType);
    }

    @Override
    public List<OptionItem> getRanking(int dayNum, float upperLimit, float lowerLimit, String sortType) {
        List<Option> optsSorted = optionDAO.findCurrentOptions(SortUtil.sortBy(new SortDTO(sortType))); //排序
        List<Option> optsAfterFilter = optsSorted.stream().filter(option -> option.getQuoteChange() >= lowerLimit && option.getQuoteChange() <= upperLimit).collect(Collectors.toList()); //筛选
        List<OptionItem> res = new ArrayList<>();
        long rank = 0;
        for (Option opt : optsAfterFilter){
            res.add(new OptionItem(rank++, opt));
        }
        return res;
    }

    @Override
    public ResultMessage addInterestedOption(String optionAbbr, String userId) {
        return optionDAO.addInterestedOption(optionAbbr, userId);
    }

    @Override
    public ResultMessage deleteInterestedOption(String optionAbbr, String userId) {
        return optionDAO.deleteInterestedOption(optionAbbr, userId);
    }

    @Override
    public List<Option> findInterestedOptions(String userId) {
        return optionDAO.findInterestedOptions(userId);
    }

    @Override
    public List<User> findInterestingUsers(String optionAbbr) {
        return optionDAO.findInterestingUsers(optionAbbr);
    }
}
