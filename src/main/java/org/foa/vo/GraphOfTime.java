package org.foa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JsonFormat
    @DateTimeFormat
    private T time;

    private Double value;
}
