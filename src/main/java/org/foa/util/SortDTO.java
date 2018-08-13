package org.foa.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王川源
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortDTO {

    /**
     * 排序方式，默认为降序
     */
    private String orderType = "desc";

    /**
     * 排序字段
     */
    private String orderField;

    public SortDTO(String orderField){
        this.orderType = "desc";
        this.orderField = orderField;
    }
}
