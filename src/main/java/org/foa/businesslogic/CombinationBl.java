package org.foa.businesslogic;

import com.google.gson.Gson;
import org.foa.entity.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/CombinationBl")
public class CombinationBl {
    private Gson gson=new Gson();




    @RequestMapping("/setCurrentCombination")
    private String setCurrentCombination2(@RequestParam String userJson, @RequestParam String combinationJson){
        User user=gson.fromJson(userJson,User.class);
        Combination combination=gson.fromJson(combinationJson,Combination.class);
        //TODO
        return null;
    }

    @RequestMapping("/getCurrentCombination")
    private Combination getCurrentCombination(@RequestParam String userJson ){
        User user=gson.fromJson(userJson,User.class);
        //TODO
        return null;
    }

    @RequestMapping("/addInterestedOption")
    private String addInterestedOption(@RequestParam String optionJson, @RequestParam String userJson){
        User user=gson.fromJson(userJson,User.class);
        Option option =gson.fromJson(optionJson, Option.class);
        //TODO
        return null;
    }

    @RequestMapping("/deleteInterestedOption")
    private String deleteInterestedOption(@RequestParam String optionJson, @RequestParam String userJson){
        User user=gson.fromJson(userJson,User.class);
        Option option =gson.fromJson(optionJson, Option.class);
        //TODO
        return null;
    }


    @RequestMapping("/modifyInterestedOption")
    private String modifyInterestedOption(@RequestParam String optionJson, @RequestParam String userJson){
        User user=gson.fromJson(userJson,User.class);
        Option option =gson.fromJson(optionJson, Option.class);
        //TODO
        return null;
    }

    @RequestMapping("/findInterestedOptions")
    private ArrayList<Option> findInterestedOptions(@RequestParam String  userJson){
        User user=gson.fromJson(userJson,User.class);
        //TODO
        return null;
    }
    @RequestMapping("/findInterestingUsers")
    private ArrayList<User> findInterestingUsers(@RequestParam String optionJson){
        Option option =gson.fromJson(optionJson, Option.class);
        //TODO
        return null;
    }
    @RequestMapping("/evaluateCombination")
    private Evaluation evaluateCombination(@RequestParam String combinationJson){
        Combination combination=gson.fromJson(combinationJson,Combination.class);
        //TODO
        return null;
    }

    @RequestMapping("/getRankedCombinations")
    ArrayList<Combination> getRankedCombinations(@RequestParam String termsJson){
        ArrayList<OptionItem> terms=gson.fromJson(termsJson,ArrayList.class);
        //TODO
        return null;
    }


    /**
     * 1. ResultMessage setCurrentCombination(User user, Combination combination);
     * 2. Combination getCurrentCombination(User user);//每⼀个⽤户仅能有⼀个当前期权组合
     * Combination包括两项Option，及其购买数量（默认购买⽐例为1:1:1:1，故只需⼀个int），盈利指
     * 标Evaluation
     * 3. ResultMessage addInterestedOption(Option option, User user);//InterestedOption对应“我的期权”
     * 4. ResultMessage deleteInterestedOption(Option option, User user);
     * 5. ResultMessage modifyInterestedOption(Option option, User user);
     * 6. List<Option> findInterestedOptions(User user);
     * 7. List<User> findInterestingUsers(Option option);
     * 8. Evaluation evaluateCombination(Combination combination);
     * 期权组合的盈利指标暂时仅有Term1-Term2的绝对值
     * 9. List<Combination> getRankedCombinations(List<RankTerm> terms);
     * RankTerm为接⼝，暂时仅有DefaultRank（按盈利指标降序）⼀种实现
     */



    /**
     * Make singleton
     * Usage Instance: CombinationBl.combinationBl().functionName();
     */
    private CombinationBl(){};
    private CombinationBl combinationBl;
    public CombinationBl combinationBl(){
        if (combinationBl==null){
            combinationBl=new CombinationBl();
        }
        return combinationBl;
    }
}
