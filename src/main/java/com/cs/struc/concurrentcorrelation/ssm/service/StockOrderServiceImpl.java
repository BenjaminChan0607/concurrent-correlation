package com.cs.struc.concurrentcorrelation.ssm.service;

import com.cs.struc.concurrentcorrelation.ssm.dao.StockDao;
import com.cs.struc.concurrentcorrelation.ssm.dao.StockOrderDao;
import com.cs.struc.concurrentcorrelation.ssm.entity.StockDO;
import com.cs.struc.concurrentcorrelation.ssm.entity.StockOrderDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author benjaminChan
 * @date 2018/12/4 0004 下午 5:39
 */
@Service
@Transactional
@Slf4j
public class StockOrderServiceImpl implements StockOrderService {

    @Autowired
    private StockDao stockDao;
    @Autowired
    private StockOrderDao stockOrderDao;

    @Override
    public int createWrongOrder(int sid) {

        //校验库存
        StockDO stock = checkStock(sid);

        //扣库存
//        synchronized (StockOrderServiceImpl.class) {
//            saleStock(stock);
//        }
        //乐观锁扣库存
        saleStockOptimistic(stock);

        //创建订单
        int id = createOrder(stock);

        log.info("----ssm finish----.");
        return id;
    }

    private void saleStockOptimistic(StockDO stock) {
        int count = stockDao.updateStockByOptimistic(stock.getSale() + 1, stock.getVersion() + 1, stock.getSale(), stock.getVersion());
        if (count == 0) {
            throw new RuntimeException("并发更新库存失败");
        }
    }

    private StockDO checkStock(int sid) {
        StockDO stock = stockDao.findById(sid).orElse(null);
        if (stock.getSale().equals(stock.getCount())) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    private StockDO saleStock(StockDO stock) {
        stock.setSale(stock.getSale() + 1);
        return stockDao.save(stock);
    }

    private int createOrder(StockDO stock) {
        StockOrderDO stockOrderDO = new StockOrderDO();
        stockOrderDO.setSid(stock.getId());
        stockOrderDO.setName(stock.getName());
        stockOrderDO.setCreate_time(LocalDateTime.now());
        stockOrderDao.save(stockOrderDO);
        return stockOrderDO.getId();
    }
}
