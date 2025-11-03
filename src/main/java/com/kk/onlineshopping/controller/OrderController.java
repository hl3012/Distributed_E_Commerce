package com.kk.onlineshopping.controller;

import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;
import com.kk.onlineshopping.db.po.OnlineShoppingOrder;
import com.kk.onlineshopping.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.dao.OnlineShoppingOrderDao;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
public class OrderController {
    @Resource
    OrderService orderService;

    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;

    @RequestMapping("/commodity/buy/{userId}/{commodityId}")
    public String buyCommodity(@PathVariable("userId") long userId,
                               @PathVariable("commodityId") long commodityId,
                               Map<String, Object> requestMap) {
//        OnlineShoppingOrder onlineShoppingOrder = orderService.processOrderDistributedLock(commodityId, userId);
//        OnlineShoppingOrder onlineShoppingOrder = orderService.processOrderSQL(commodityId, userId);
//        OnlineShoppingOrder onlineShoppingOrder = orderService.processOrder(commodityId, userId);
//        OnlineShoppingOrder onlineShoppingOrder = orderService.processOrderRedis(commodityId, userId);
        OnlineShoppingOrder onlineShoppingOrder = orderService.processOrderFinal(commodityId, userId);

        if (onlineShoppingOrder == null) {
            requestMap.put("resultInfo", "failed order creation");
            requestMap.put("orderNum", "");
        } else {
            requestMap.put("resultInfo", "successful order creation");
            requestMap.put("orderNum", onlineShoppingOrder.getOrderNo());
        }
        return "order_result";
    }

    @RequestMapping("/commodity/orderQuery/{orderNo}")
    public String orderQuery(@PathVariable String orderNo, Map<String, Object> requestMap) {
        OnlineShoppingOrder order = orderService.getOrder(orderNo);
        requestMap.put("order", order);
        OnlineShoppingCommodity onlineShoppingCommodity = onlineShoppingCommodityDao.queryCommodityById(order.getCommodityId());
        requestMap.put("commodity", onlineShoppingCommodity);
        return "order_check";
    }

    @RequestMapping("/commodity/payOrder/{orderNumber}")
    public String payOrder(@PathVariable String orderNumber, Map<String, Object> requestMap) {
        orderService.payOrderProcess(orderNumber);
        return orderQuery(orderNumber, requestMap);
    }
}
