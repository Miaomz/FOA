package org.foa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author miaomuzhi
 * @since 2018/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionItem {

    private long ranking;

    private Option option;

}
