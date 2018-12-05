package com.cs.struc.concurrentcorrelation.ssm.dao;

import com.cs.struc.concurrentcorrelation.ssm.entity.StockDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author benjaminChan
 * @date 2018/12/4 0004 下午 5:45
 */
public interface StockDao extends JpaRepository<StockDO,Integer> {

    @Modifying
    @Query(value = "update stock set sale = ? , version = ? where sale = ? and version = ?",nativeQuery = true)
    int updateStockByOptimistic(int i, int i1, Integer sale, Integer version);
}
