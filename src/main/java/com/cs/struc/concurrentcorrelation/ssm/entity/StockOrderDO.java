package com.cs.struc.concurrentcorrelation.ssm.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author benjaminChan
 * @date 2018/12/4 0004 下午 5:27
 */
@Table(name = "stock_order")
@Entity
@Data
public class StockOrderDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer sid;
    private LocalDateTime create_time;
}
