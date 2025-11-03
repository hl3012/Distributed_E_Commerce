package com.kk.onlineshopping.controller;

import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class CommodityController {

    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;

    @RequestMapping("/addItem")
    public String addCommodity() {
        return "add_commodity";
    }

    @PostMapping("/commodities")
    public String handleAddCommodity(@RequestParam("commodityId") long commodityId,
                                     @RequestParam("commodityName") String commodityName,
                                     @RequestParam("commodityDesc") String commodityDesc,
                                     @RequestParam("price") int price,
                                     @RequestParam("availableStock") int availableStock,
                                     @RequestParam("creatorUserId") long creatorUserId,
                                     Map<String, Object> resultMap) {
        OnlineShoppingCommodity commodity = OnlineShoppingCommodity.builder()
                .commodityId(commodityId)
                .commodityName(commodityName)
                .commodityDesc(commodityDesc)
                .price(price)
                .availableStock(availableStock)
                .creatorUserId(creatorUserId)
                .totalStock(availableStock)
                .lockStock(0)
                .build();

        onlineShoppingCommodityDao.insertCommodity(commodity);
        resultMap.put("Item", commodity);
        return "add_commodity_success";
    }

    @GetMapping("/commodities/{sellerId}")
    public String listCommoditiesByUserId(@PathVariable long sellerId, Map<String, Object> resultMap) {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = onlineShoppingCommodityDao.listCommoditiesByUserId(sellerId);
        resultMap.put("commodities", onlineShoppingCommodities);
        return "list_items";
    }

    @GetMapping({"commodities","/"})
    public String listCommodities(Map<String, Object> resultMap) {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = onlineShoppingCommodityDao.listCommodities();
        resultMap.put("commodities", onlineShoppingCommodities);
        return "list_items";
    }

//    @GetMapping("/")
//    public String listCommoditiesHome(Map<String, Object> resultMap) {
//        List<OnlineShoppingCommodity> onlineShoppingCommodities = onlineShoppingCommodityDao.listCommodities();
//        resultMap.put("commodities", onlineShoppingCommodities);
//        return "list_items";
//    }

    @GetMapping("item/{commodityId}")
    public String getCommodity(@PathVariable("commodityId") long commodityId, Map <String, Object> resultMap) {
        OnlineShoppingCommodity onlineShoppingCommodity = onlineShoppingCommodityDao.queryCommodityById(commodityId);
        resultMap.put("commodities", onlineShoppingCommodity);
        return "list_details";
    }
}
