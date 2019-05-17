package xyz.linye.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.OrderService;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbOrderItem;
import xyz.e3mall.manager.pojo.TbOrderShipping;
import xyz.e3mall.manager.pojo.TbUser;
import xyz.linye.cartservice.CartService;
import xyz.pojo.TbOrderInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    /**
     *          由于没有前台页面，直接写死一个订单对象
   * @param tbOrderInfo
     * @param request
     * @return
     */
    @RequestMapping(value = "order/order")
    @ResponseBody
    public E3Result createOrder(TbOrderInfo tbOrderInfo, HttpServletRequest request){
        /**
         * 模拟接口，写死对象，测试
         */
        tbOrderInfo = new TbOrderInfo();


        TbOrderItem tbOrderItem = new TbOrderItem();
        List<TbOrderItem> tbOrderItemList = new ArrayList<>();
        TbOrderShipping tbOrderShipping = new TbOrderShipping();

        tbOrderInfo.setOrderItemList(tbOrderItemList);
        tbOrderInfo.setTbOrderShipping(tbOrderShipping);


        //从request中取出user对象
        TbUser user = (TbUser) request.getAttribute("user");

        //补全用户信息
        tbOrderInfo.setUserId(user.getId());
        tbOrderInfo.setBuyerNick(user.getUsername());

        //调用服务生成订单
        E3Result e3Result = orderService.createOrder(tbOrderInfo);

        //判断是否生成成功
        if(e3Result.getStatus() == 200){
            //清空购物车
            cartService.deleteAllCart(user.getId().intValue());
        }

        return E3Result.ok(e3Result.getData());
    }

}
