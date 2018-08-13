package org.foa.util;

import org.springframework.data.domain.Sort;

/**
 * @author 王川源
 */
public class SortUtil {

    public static Sort sortBy(SortDTO... dtos) {
        Sort result = null;
        for (int i = 0; i < dtos.length; i++) {
            SortDTO dto = dtos[i];
            if (result == null) {
                result = new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }
}
