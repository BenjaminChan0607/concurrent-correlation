package com.cs.struc.concurrentcorrelation.ssm.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author benjaminChan
 * @date 2018/12/4 0004 下午 5:27
 */
@Table(name = "stock")
@Entity
@Data
public class StockDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer count;
    private Integer sale;
    private Integer version;
}
