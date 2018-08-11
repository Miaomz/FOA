package org.foa.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
public class JsonUtil {

    private JsonUtil() {}

    /**
     * @param object Java对象
     * @return Json格式字符串
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * @param jsonString Json格式字符串
     * @param tClass     Java对象的class，请传递引用的真正类型
     * @param <T> 范型
     * @return Java对象
     */
    public static <T> T toObject(String jsonString, Class<T> tClass) {
        return JSON.parseObject(jsonString, tClass);
    }

    public static <T> List<T> toArray(String jsonString, Class<T> tClass){
        return JSON.parseArray(jsonString, tClass);
    }

}
