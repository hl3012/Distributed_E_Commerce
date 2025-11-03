package com.kk.onlineshopping.db.dao;

import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;

import java.util.List;

public interface OnlineShoppingCommodityDao {
    int deleteCommodityById(Long commodityId);

    int insertCommodity(OnlineShoppingCommodity record);

    OnlineShoppingCommodity queryCommodityById(Long commodityId);

    int updateCommodity(OnlineShoppingCommodity record);

    List<OnlineShoppingCommodity> listCommoditiesByUserId(long userId);
    List<OnlineShoppingCommodity> listCommodities();

    int deductStockWithCommodityId(long commodityId);
    int revertStockWithCommodityId(long commodityId);
}
