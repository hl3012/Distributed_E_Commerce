package com.kk.onlineshopping.db.mappers;

import com.kk.onlineshopping.db.po.OnlineShoppingUser;

public interface OnlineShoppingUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(OnlineShoppingUser record);

    int insertSelective(OnlineShoppingUser record);

    OnlineShoppingUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(OnlineShoppingUser record);

    int updateByPrimaryKey(OnlineShoppingUser record);
}