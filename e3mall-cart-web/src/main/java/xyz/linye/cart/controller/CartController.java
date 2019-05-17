package xyz.linye.cart.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.utils.CookieUtils;
import xyz.e3mall.common.utils.JsonUtils;
import xyz.e3mall.manager.ItemService;
import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.manager.pojo.TbUser;
import xyz.linye.cartservice.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;

    @Value("${COOKIE_TIME}")
    int COOKIE_TIME;

    @Value("${CART_COOKIE}")
    String CART_COOKIE;


    /**
     * 添加购物车
     *
     * @param itemId
     * @param num
     */
    @RequestMapping(value = "cart/add/{itemId}")
    @ResponseBody
    public E3Result addCart(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("itemId") Integer itemId, Integer num) {//添加到购物车的商品id和数量

        //判断用户是否登录，从request域中取出购物车
        TbUser user = (TbUser) request.getAttribute("user");

        //若登录，则将购物车存放到redis中
        if (user != null) {
            //调用cart服务，添加到redis中
            E3Result e3Result = cartService.addCart(user.getId(), itemId, num);

            if (e3Result.getStatus() == 200) {
                return E3Result.ok("已成功添加购物车");
            }


        }


        //1.从cookie拿到购物车
        List<TbItem> itemList = getItemListFromCookie(request, CART_COOKIE);

        //标志变量
        boolean flag = true;

        //2.判断购物车中是否有添加的商品
        for (TbItem item : itemList) {
            //遍历集合，若有相同的商品则添加数量
            if (item.getId() == itemId.intValue()) {
                //数量相加
                item.setNum(item.getNum() + num);
                flag = false;
            }
            break;
        }

        //3.若不在购物车中，则直接添加到购物车中
        if (flag) {
            //调用商品服务获取商品pojo，修改商品数量后放入购物车中
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            itemList.add(tbItem);
        }

        //4.写入cookie
        CookieUtils.setCookie(request, response, CART_COOKIE,
                JsonUtils.objectToJson(itemList), COOKIE_TIME, true);

        //5.返回
        return E3Result.ok(itemList);
    }

    /**
     * 显示购物车所有的商品列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "cart/cart")
    @ResponseBody
    public List<TbItem> showCartList(HttpServletRequest request,HttpServletResponse response) {
        //从cookie取到购物车
        List<TbItem> itemListFromCookie = getItemListFromCookie(request, CART_COOKIE);

        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");

        //若登录
        if(user != null){
            //调用服务，传输cookie中的商品列表,存放到redis中
            List<TbItem> tbItemList = cartService.mergeCart(user.getId().intValue(), itemListFromCookie);

            //删除cookie中的购物车
            CookieUtils.deleteCookie(request,response,CART_COOKIE);

            return tbItemList;

        }

        //若没有登录，则直接从cookie获取商品列表
        return itemListFromCookie;
    }


    //工具方法：从cookie中获取购物车列表
    public List<TbItem> getItemListFromCookie(HttpServletRequest request,
                                              String cookieName) {
        //获取到购物车字符串
        String cart_cookie = CookieUtils.getCookieValue(request, cookieName, true);

        //判断是否为空后返回
        if (StringUtils.isNotBlank(cart_cookie)) {
            return JsonUtils.jsonToList(cart_cookie, TbItem.class);
        }

        //为空则返回空集合
        return new ArrayList<>();

    }

    /**
     * 修改购物车数量接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable("itemId") Integer itemId,
                                  @PathVariable("num") Integer num) {

        //判断用户是否登录，从request域中取出购物车
        TbUser user = (TbUser) request.getAttribute("user");

        if(user != null){
            E3Result e3Result = cartService.updateCartNum(user.getId().intValue(), itemId, num);
            return e3Result;
        }

        //从cookie中取商品列表
        List<TbItem> tbItemList = getItemListFromCookie(request, CART_COOKIE);

        for (TbItem tbItem : tbItemList) {
            //更新商品数量
            if (tbItem.getId() == itemId.intValue()) {
                tbItem.setNum(num);
                break;
            }
        }
        //写回cookie中
        CookieUtils.setCookie(request, response, CART_COOKIE,
                JsonUtils.objectToJson(tbItemList), COOKIE_TIME, true);

        return E3Result.ok();
    }


    /***
     * 删除购物车中指定商品
     * @param request
     * @param response
     * @param itemId
     * @return
     */
    @RequestMapping(value = "cart/delete/{itemId}")
    @ResponseBody
    public E3Result deleteCartItem(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("itemId") Integer itemId) {

        //判断用户是否登录，从request域中取出购物车
        TbUser user = (TbUser) request.getAttribute("user");

        if(user != null){
            E3Result e3Result = cartService.deleteCartNum(user.getId().intValue(), itemId);
            return e3Result;
        }

        //从cookie中取商品列表
        List<TbItem> tbItemList = getItemListFromCookie(request, CART_COOKIE);

        for (TbItem tbItem : tbItemList) {
            //更新商品数量
            if (tbItem.getId() == itemId.intValue()) {
                tbItemList.remove(tbItem);
                break;
            }
        }
        //写回cookie中
        CookieUtils.setCookie(request, response, CART_COOKIE,
                JsonUtils.objectToJson(tbItemList), COOKIE_TIME, true);

        return E3Result.ok();
    }


}
