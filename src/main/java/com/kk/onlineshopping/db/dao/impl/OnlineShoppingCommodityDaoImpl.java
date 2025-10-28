package com.kk.onlineshopping.db.dao.impl;

import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.mappers.OnlineShoppingCommodityMapper;
import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OnlineShoppingCommodityDaoImpl implements OnlineShoppingCommodityDao {
    @Resource
    OnlineShoppingCommodityMapper mapper;

    @Override
    public int deleteCommodityById(Long commodityId) {
        return mapper.deleteByPrimaryKey(commodityId);
    }

    @Override
    public int insertCommodity(OnlineShoppingCommodity record) {
        return mapper.insert(record);
    }

    @Override
    public OnlineShoppingCommodity queryCommodityById(Long commodityId) {
        return mapper.selectByPrimaryKey(commodityId);
    }

    @Override
    public int updateCommodity(OnlineShoppingCommodity record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public List<OnlineShoppingCommodity> listCommoditiesByUserId(long userId) {
        return mapper.listCommoditiesByUserId(userId);
    }
}
