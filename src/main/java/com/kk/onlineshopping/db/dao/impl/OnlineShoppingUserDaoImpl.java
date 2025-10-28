package com.kk.onlineshopping.db.dao.impl;

import com.kk.onlineshopping.db.dao.OnlineShoppingUserDao;
import com.kk.onlineshopping.db.mappers.OnlineShoppingUserMapper;
import com.kk.onlineshopping.db.po.OnlineShoppingUser;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OnlineShoppingUserDaoImpl implements OnlineShoppingUserDao {
    @Resource
    OnlineShoppingUserMapper mapper;

    @Override
    public int deleteUserById(Long userId) {
        return mapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insertUser(OnlineShoppingUser user) {
        return mapper.insert(user);
    }

    @Override
    public OnlineShoppingUser queryUserById(Long userId) {
        return mapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateUser(OnlineShoppingUser user) {
        return mapper.updateByPrimaryKey(user);
    }
}
