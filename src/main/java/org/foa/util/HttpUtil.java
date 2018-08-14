package org.foa.util;

import org.foa.entity.Option;
import org.foa.entity.OptionType;
import org.foa.entity.ValueState;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author miaomuzhi
 * @since 2018/8/12
 */
public class HttpUtil {

    private static final String AVAILABLE_MONTHS = "http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getStockName";
    private static final String REMAINDER_DAY = "http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getRemainderDay?date=";
    /**
     * suffix's form is CON_OP_[OptionCode]
     */
    private static final String OPT_CON = "http://hq.sinajs.cn/list=";
    /**
     * suffix indicate date, whose form is '1808', if the date is 2018-08
     */
    private static final String UP_OPTS = "http://hq.sinajs.cn/list=OP_UP_510050";
    /**
     * suffix indicate date, whose form is '1808', if the date is 2018-08
     */
    private static final String DOWN_OPTS = "http://hq.sinajs.cn/list=OP_DOWN_510050";


    /**
     *
     * @param year 年份，从公元0年计数
     * @param month 月份，从1计数
     * @return 字符串，形式如1808，在2018年8月时
     */
    private static String toDateForm(int year, int month) {
        if (month < 1 || month > 12){
            throw new DateTimeException(String.valueOf(year) + ", " + month);
        }

        String monthTemp = String.valueOf(month);
        if (monthTemp.length() == 1) {
            monthTemp = "0" + monthTemp;
        }

        return String.valueOf(year).substring(2, 4) + monthTemp;
    }

    /**
     * 将json获得字符串的转换为请求需要的格式
     * @param date 如2018-08
     * @return 如1808
     */
    private static String toDateForm(String date){
        String[] rawList = date.split("-");
        return toDateForm(Integer.valueOf(rawList[0]), Integer.valueOf(rawList[1]));
    }



    /**
     * get the months when there are option chains
     * @return list of dates whose forms are like '2018-08'
     */
    public static List<String> getAvailableMonths(){
        String resText = getResponseWithoutBody(AVAILABLE_MONTHS);
        if (resText == null) {
            return new ArrayList<>();
        }

        int index = resText.indexOf("contractMonth");
        index += "contractMonth\":[".length();

        List<String> result = new ArrayList<>();
        while (resText.charAt(index-1) != ']'){
            result.add(resText.substring(index+1, index+8));
            index += 10;
        }
        return result;
    }



    /**
     * get option of specific type and due according to month
     * @param type up or down
     * @param dateForm like '1808' if it is 2018-08
     * @return list of option codes, like 'CON_OP_10001385'
     */
    public static List<String> getOptionsOfDate(OptionType type, String dateForm){
        String url;
        if (type == OptionType.UP){
            url = UP_OPTS + dateForm;
        } else {//type = Option.DOWN
            url = DOWN_OPTS + dateForm;
        }

        String res = getResponseWithoutBody(url);
        if (res == null){
            return new ArrayList<>();
        }
        String[] optCodes = res.substring(res.indexOf('"')+1, res.length()-3).split(",");
        return new ArrayList<>(Arrays.asList(optCodes));
    }

    /**
     * 得到具体合约到期日
     * @param dateForm 合约到期月，形如'2018-08'，如果对应日期为2018-08
     * @return 到期日
     */
    public static LocalDate getRemainderDays(String dateForm){
        String resp = getResponseWithoutBody(REMAINDER_DAY + dateForm);
        if (resp == null) {
            return null;
        }

        //remove useless part
        resp = resp.substring(resp.indexOf("expireDay") + "expireDay\":\"".length());
        resp = resp.substring(0, resp.indexOf('"'));
        return LocalDate.parse(resp);
    }


    /**
     *
     * @param optionCode 合约代码，形如'CON_OP_10001385'（包含CON_OP_的前缀）
     * @param optionType 期权类型，因为网络编码问题，必须在参数中传递
     * @param dateForm 合约到期月，形如'2018-08'，如果对应日期为2018-08
     * @return 期权合约
     */
    public static Option getOptionById(String optionCode, OptionType optionType, String dateForm){
        String resp = getResponseWithoutBody(OPT_CON + optionCode);
        String furtherData = getResponseWithoutBody(OPT_CON + optionCode.replace("OP", "SO"));
        if (resp == null || furtherData == null){
            return null;
        }

        //remove useless characters
        resp = resp.substring(resp.indexOf('"') + 1);
        resp = resp.substring(0, resp.indexOf('"'));

        String[] numbers = resp.split(",");
        Option option = new Option();
        option.setOptionCode(optionCode);
        option.setOptionType(optionType);

        option.setBidVolume(Integer.valueOf(numbers[0]));//买量
        option.setBidPrice(Double.valueOf(numbers[1]));//买价
        option.setLatestPrice(Double.valueOf(numbers[2]));//最新价
        option.setSellPrice(Double.valueOf(numbers[3]));//卖价
        option.setSellVolume(Integer.valueOf(numbers[4]));//卖量
        option.setPosition(Integer.valueOf(numbers[5]));//持仓量
        option.setQuoteChange(Double.valueOf(numbers[6])/100);//涨跌幅，取值范围0~1.0
        option.setExecPrice(Double.valueOf(numbers[7]));//行权价

        //解决网路中文编码不明的问题
        String abbr = numbers[37];
        String monthStr = dateForm.split("-")[1];
        if (monthStr.charAt(0) == '0'){
            monthStr = monthStr.substring(1);
        }
        abbr = "50ETF" + (optionType == OptionType.UP ? "购" : "沽") + monthStr + "月" + abbr.substring(abbr.length()-4);
        option.setOptionAbbr(abbr);

        option.setAmplitude(Double.valueOf(numbers[38])/100);//振幅，取值范围0~1.0
        option.setVolume(Integer.valueOf(numbers[41]));//成交量

        //计算价值状态
        ValueState state = ValueState.FLAT;
        if (optionType == OptionType.UP && option.getLatestPrice() > option.getExecPrice()
                || optionType == OptionType.DOWN && option.getLatestPrice() < option.getExecPrice()){
            state = ValueState.REAL;
        } else if (optionType == OptionType.UP && option.getLatestPrice() < option.getExecPrice()
                || optionType == OptionType.DOWN && option.getLatestPrice() > option.getExecPrice()){
            state = ValueState.VIRTUAL;
        }
        option.setValueState(state);

        //进一步获得信息
        furtherData = furtherData.substring(furtherData.indexOf('"') + 1);
        furtherData = furtherData.substring(0, furtherData.indexOf('"'));
        List<String> furtherNumbers = new ArrayList<>(Arrays.asList(furtherData.split(",")));
        furtherNumbers.removeIf(furtherNumber -> furtherNumber == null ||furtherNumber.equals(""));
        option.setGamma(Double.valueOf(furtherNumbers.get(3)));
        option.setTheta(Double.valueOf(furtherNumbers.get(4)));
        option.setVega(Double.valueOf(furtherNumbers.get(5)));
        option.setInteriorRange(Double.valueOf(furtherNumbers.get(6)));

        //计算日期
        option.setExpireDay(getRemainderDays(dateForm));
        int remainderDays = Period.between(LocalDate.now(), option.getExpireDay()).getDays();
        option.setRemindedBusinessDays(remainderDays);
        option.setRemindedNaturalDays(remainderDays);
        return option;
    }

    public static String getResponseWithoutBody(String urlStr){
        String resText = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append(System.lineSeparator());
            }
            rd.close();
            resText =  new String(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resText;
    }

    public static List<Option> recordAllOptions(){
        List<Option> result = new ArrayList<>();
        List<String> availableMonths = getAvailableMonths();
        List<String> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < availableMonths.size()-1; i++) {
            for (int j = i+1; j < availableMonths.size(); j++) {
                if (availableMonths.get(i).equals(availableMonths.get(j))){
                    toBeRemoved.add(availableMonths.get(i));
                    break;
                }
            }
        }
        availableMonths.removeAll(toBeRemoved);

        for (String availableMonth : availableMonths) {
            //get ids
            List<String> upOpts = getOptionsOfDate(OptionType.UP, toDateForm(availableMonth));
            List<String> downOpts = getOptionsOfDate(OptionType.DOWN, toDateForm(availableMonth));
            //remove anomalies which generated for unknown reasons
            upOpts.removeIf(upOpt -> !upOpt.contains("CON_OP_"));
            downOpts.removeIf(downOpt -> !downOpt.contains("CON_OP_"));

            //get entities
            List<Option> upOptions = new ArrayList<>(upOpts.size());
            upOpts.forEach(upOpt -> upOptions.add(getOptionById(upOpt, OptionType.UP, availableMonth)));
            upOptions.removeAll(Collections.singleton(null));
            List<Option> downOptions = new ArrayList<>(downOpts.size());
            downOpts.forEach(downOpt -> downOptions.add(getOptionById(downOpt, OptionType.DOWN, availableMonth)));
            downOptions.removeAll(Collections.singleton(null));

            result.addAll(upOptions);
            result.addAll(downOptions);
        }
        return result;
    }

    public static void main(String[] args) {
        //to be determined
        String path = "/Users/miaomuzhi/Downloads/opts/" + LocalDateTime.now().toString();
        StringBuilder builder = new StringBuilder();
        List<Option> options = recordAllOptions();
        options.forEach(option -> builder.append(JsonUtil.toJson(option)).append(System.lineSeparator()));
        FileUtil.writeFile(path, builder.toString());
    }
}
