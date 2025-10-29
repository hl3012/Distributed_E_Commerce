package com.kk.onlineshopping.db.dao;

import com.kk.onlineshopping.db.po.OnlineShoppingOrder;

public interface OnlineShoppingOrderDao {
    int deleteOrderById(Long orderId);

    int insertOrder(OnlineShoppingOrder record);

    OnlineShoppingOrder queryOrderById(Long orderId);
    OnlineShoppingOrder queryOrderByOrderNum(String orderNum);

    int updateOrder(OnlineShoppingOrder record);
}
