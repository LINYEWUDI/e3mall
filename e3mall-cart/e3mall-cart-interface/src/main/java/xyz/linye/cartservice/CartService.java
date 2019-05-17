package xyz.linye.cartservice;

import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbItem;

import java.util.List;

public interface CartService {

    E3Result addCart(long userId,long itemId,long num);
    List<TbItem> mergeCart(Integer userId,List<TbItem> itemListFromCookie);
    List<TbItem> getItemListFromRedis(Integer userId);
    E3Result updateCartNum(Integer userId,Integer itemId,Integer num);
    E3Result deleteCartNum(Integer userId,Integer itemId);
    E3Result deleteAllCart(Integer userId);
}
