package com.kk.onlineshopping.db.dao.impl;

import com.kk.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.kk.onlineshopping.db.mappers.OnlineShoppingOrderMapper;
import com.kk.onlineshopping.db.po.OnlineShoppingOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OnlineShoppingOrderDaoImpl implements OnlineShoppingOrderDao {
    @Resource
    OnlineShoppingOrderMapper mapper;

    @Override
    public int deleteOrderById(Long orderId) {
        return mapper.deleteByPrimaryKey(orderId);
    }

    @Override
    public int insertOrder(OnlineShoppingOrder record) {
        return mapper.insert(record);
    }

    @Override
    public OnlineShoppingOrder queryOrderById(Long orderId) {
        return mapper.selectByPrimaryKey(orderId);
    }

    @Override
    public OnlineShoppingOrder queryOrderByOrderNum(String orderNum) {
        return mapper.selectByOrderNum(orderNum);
    }

    @Override
    public int updateOrder(OnlineShoppingOrder record) {
        return mapper.updateByPrimaryKey(record);
    }
}
