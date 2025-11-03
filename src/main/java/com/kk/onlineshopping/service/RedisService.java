package com.kk.onlineshopping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;

@Slf4j
@Service
public class RedisService {
    @Resource
    private JedisPool jedisPool;

    public String setValue(String key, String value) {
        Jedis jedisClient = jedisPool.getResource();
        String res = jedisClient.set(key, value);
        jedisClient.close();
        return res;
    }

    public String getValue(String key) {
        Jedis client = jedisPool.getResource();
        String value = client.get(key);
        client.close();
        return value;
    }

    public Long deductStockWithCommodityId(String key) {
        Jedis jedis = jedisPool.getResource();
        String script =
                "if redis.call('exists', KEYS[1]) == 1 then\n"
                        + " local stock = tonumber(redis.call('get', KEYS[1]))\n"
                        + " if (stock<=0) then\n" + " return -1\n" + " end\n" + "\n"
                        + " redis.call('decr', KEYS[1]);\n" + " return stock - 1;\n"
                        + "end\n" + "\n" + "return -1;";  //lua scripts
        Long stock = -1L;
        try {
            stock = (Long) jedis.eval(script, Collections.singletonList(key), Collections.emptyList());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return stock;
    }

    public Long revertStockWithCommodityId(String key) {
        Jedis jedis = jedisPool.getResource();
        Long incr = jedis.incr(key);
        jedis.close();
        return incr;
    }

//    public long stockDeduct(String key) {
//        try (Jedis client = jedisPool.getResource()) {
//            String script = "if redis.call('exists', KEYS[1]) == 1 then\n" + " local stock = tonumber(redis.call('get', KEYS[1]))\n" + " if (stock<=0) then\n" + " return -1\n" + " end\n" + "\n" + " redis.call('decr', KEYS[1]);\n" + " return stock - 1;\n" + "end\n" + "\n" + "return -1;";
//            Long stock = (Long) client.eval(script, Collections.singletonList(key), Collections.emptyList());
//            if (stock < 0) {
//                System.out.println("There is no stock available");
//                return -1;
//            } else {
//                System.out.println("Validate and decreased redis stock, current available stock：" + stock);
//                return stock;
//            }
//        } catch (Throwable throwable) {
//            System.out.println("Exception on stockDeductValidation：" + throwable.toString());
//            return -1;
//        }
//    }

    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedisClient = jedisPool.getResource();
        String result = jedisClient.set(lockKey, requestId, "NX", "PX", expireTime); // twice of transaction time
        jedisClient.close();
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedisClient = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1]" + " then return redis.call('del', KEYS[1])" + " else return 0 end";
        Long result = (Long) jedisClient.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        jedisClient.close();
        if (result == 1L) { return true; }
        return false;
    }

    public void addToDenyList(String userId, String commodityId) {
        String key = "online_shopping:denyListUserId:" + userId;
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd(key, commodityId);
        jedisClient.close();
        log.info("Add userId: {} into denyList for commodityId: {} ", userId, commodityId);
    }


    public boolean isInDenyList(String userId, String commodityId) {
        String key = "online_shopping:denyListUserId:" + userId;
        Jedis jedisClient = jedisPool.getResource();
        Boolean sismember = jedisClient.sismember(key, commodityId);
        jedisClient.close();
        return sismember;
    }

    public void removeFromDenyList(String userId, String commodityId) {
        String key = "online_shopping:denyListUserId:" + userId;
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem(key, commodityId);
        jedisClient.close();
        log.info("Add userId: {} into denyList for commodityId: {} ", userId, commodityId);
    }
}


