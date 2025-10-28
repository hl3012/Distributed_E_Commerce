package com.kk.onlineshopping.db.dao;

import com.kk.onlineshopping.db.po.OnlineShoppingUser;

public interface OnlineShoppingUserDao {
    int deleteUserById(Long userId);

    int insertUser(OnlineShoppingUser user);

    OnlineShoppingUser queryUserById(Long userId);

    int updateUser(OnlineShoppingUser user);
}