package com.cs.struc.concurrentcorrelation.ssm.dao;

import com.cs.struc.concurrentcorrelation.ssm.entity.StockOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author benjaminChan
 * @date 2018/12/4 0004 下午 5:44
 */
public interface StockOrderDao extends JpaRepository<StockOrderDO, Integer> {

}
