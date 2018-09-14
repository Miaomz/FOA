package org.foa.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 前端数据图显示
 * 横坐标为time
 * 纵坐标为Double
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphOfTime<T> {

    private T time;

    private Double value;
}
