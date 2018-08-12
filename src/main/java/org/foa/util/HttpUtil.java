package org.foa.util;

import org.foa.entity.Option;
import org.foa.entity.OptionType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author miaomuzhi
 * @since 2018/8/12
 */
public class HttpUtil {

    private static final String AVAILABLE_MONTHS = "http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getStockName";
    private static final String REMAINDER_DAY = "http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getRemainderDay?date=201808";
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
     *
     * @param optionCode 合约代码，形如'CON_OP_10001385'
     * @return 期权合约
     */
    public static Option getOptionById(String optionCode){
        String resp = getResponseWithoutBody(OPT_CON + optionCode);
        return null;
    }

    public static String getResponseWithoutBody(String urlStr){
        String resText = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "iso8859-1");
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

    public static void main(String[] args) {
        String resp = getResponseWithoutBody(OPT_CON + "CON_OP_10001385");
        System.out.println(resp);
    }
}
