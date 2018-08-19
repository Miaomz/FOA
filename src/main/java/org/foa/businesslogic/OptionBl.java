package org.foa.businesslogic;


import com.google.gson.Gson;
import org.foa.entity.Option;
import org.foa.entity.OptionItem;
import org.foa.entity.OptionType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/OptionBl")
public class OptionBl {
    //private Gson gson= new Gson();


    @RequestMapping("/getOption")
    private Option getOption(@RequestParam String optionCode){
        //TODO
        return null;
    }

    @RequestMapping("/addOption")
    private String addOption(@RequestParam String username,@RequestParam String optionCode){
        //TODO
        return null;
    }

    @RequestMapping("/getCategory")
    private ArrayList<Option> getCategory(@RequestParam OptionType optionType){
        //TODO
        return null;
    }

    @RequestMapping("/getRanking")
    private ArrayList <OptionItem>getRanking(@RequestParam int dayNum,@RequestParam  float upperLimit ,@RequestParam float lowerLimit,@RequestParam String sortType){
        //TODO
        return null;
    }



    /**
     * Make singleton.
     * Usage Instance: OptionBl.optionBl().functionName();
     */
    private OptionBl(){};
    private OptionBl optionBl;
    public OptionBl optionBl(){
        if (optionBl==null){
            optionBl=new OptionBl();
        }
        return optionBl;
    }
}
