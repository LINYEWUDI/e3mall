package xyz.e3mall.cart.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.utils.JsonUtils;
import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.linye.cartservice.CartService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    JedisCluster jedisCluster;

    @Autowired
    TbItemMapper tbItemMapper;

    @Value("${REDIS_CART_PRE}")
    String REDIS_CART_PRE;

    /**
     * 在redis中添加购物车
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public E3Result addCart(long userId,long itemId,long num) {
        //从redis获取购物车
        String itemJson = jedisCluster.hget(REDIS_CART_PRE+":"+userId+"", itemId+"");

        //转成成商品pojo
        TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);

        //若购物车中存在商品
        if(tbItem != null){
            tbItem.setNum((int)(tbItem.getNum()+num));
            //放入redis中
            jedisCluster.hset(REDIS_CART_PRE+":"+userId+"", itemId+"",JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }


        //若不存在,先直接耦合，后面修改成消息队列
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        item.setNum((int)num);

        //放入redis中
        jedisCluster.hset(REDIS_CART_PRE+":"+userId+"", itemId+"",JsonUtils.objectToJson(item));

        return E3Result.ok();
    }

    /**
     * 合并cookie中的购物车和redis中的
     * @param itemListFromCookie
     * @return
     */
    @Override
    public List<TbItem> mergeCart(Integer userId,List<TbItem> itemListFromCookie) {
        //若cookie中的给购物车不为空
        if(itemListFromCookie != null){
            for (TbItem tbItem : itemListFromCookie) {
                addCart(userId,tbItem.getId(),tbItem.getNum());
            }
        }


        List<TbItem> itemListFromRedis = getItemListFromRedis(userId);
        return itemListFromRedis;
    }


    /**
     * 修改商品数量
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public E3Result updateCartNum(Integer userId, Integer itemId, Integer num) {
        //获取需要修改的商品的json字符串
        String itemJson = jedisCluster.hget(REDIS_CART_PRE+":"+userId+"", itemId+"");

        TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);

        tbItem.setNum(num);

        jedisCluster.hset(REDIS_CART_PRE+":"+userId+"", itemId+"",JsonUtils.objectToJson(tbItem));


        return E3Result.ok("修改成功！");
    }

    /**
     * 删除购物车中的商品
     * @param userId
     * @return
     */
    @Override
    public E3Result deleteCartNum(Integer userId,Integer itemId) {
        //拿到redis中所有的购物车列表
        List<TbItem> itemListFromRedis = getItemListFromRedis(userId);

        //遍历购物车
        for (TbItem item : itemListFromRedis) {
            if(itemId == item.getId().intValue()){
                itemListFromRedis.remove(item);
                break;
            }
        }
        jedisCluster.hset(REDIS_CART_PRE+":"+userId+"", itemId+"",JsonUtils.objectToJson(itemListFromRedis));

        return E3Result.ok("删除成功！");
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    @Override
    public E3Result deleteAllCart(Integer userId) {
        jedisCluster.del(REDIS_CART_PRE + ":" + userId + "");
        return E3Result.ok("删除成功！");
    }


    /**
     * 从redis中获取商品列表
     * @return
     */
    @Override
    public List<TbItem> getItemListFromRedis(Integer userId) {
        //取到redis中的购物车列表
        List<String> stringList = jedisCluster.hvals(REDIS_CART_PRE + ":" + userId + "");

        //创建空的购车车列表
        List<TbItem> itemList = new ArrayList<>();

        for (String str : stringList) {
            TbItem tbItem = JsonUtils.jsonToPojo(str, TbItem.class);
            itemList.add(tbItem);
        }

        return itemList;
    }

}
