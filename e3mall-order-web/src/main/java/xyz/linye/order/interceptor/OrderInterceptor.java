package xyz.linye.order.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.omg.CosNaming.IstringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.utils.CookieUtils;
import xyz.e3mall.common.utils.JsonUtils;
import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.manager.pojo.TbUser;
import xyz.e3mall.sso.UserService;
import xyz.linye.cartservice.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提交订单前进行拦截
 */
public class OrderInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Value("${CART_COOKIE}")
    String CART_COOKIE;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从cookie中获取token
        String tokenId = CookieUtils.getCookieValue(request, "COOKIE_TOKEN_ID");
        //判断token
        if (StringUtils.isBlank(tokenId)) {
            response.sendRedirect("http://localhost/v1/sso");
            return false;
        }


        //判断是否身份是否过期
        E3Result e3Result = userService.getTokenUser("SESSION_ID:" + tokenId);
        if(e3Result.getStatus() != 200){
            response.sendRedirect("http://localhost/v1/sso");
            return false;
        }

        //拿到用户实体
        TbUser user = (TbUser) e3Result.getData();

        //获取cookie中的购物车
        String cartInCookie = CookieUtils.getCookieValue(request, CART_COOKIE, true);

        //合并到redis中
        cartService.mergeCart(user.getId().intValue(), JsonUtils.jsonToList(cartInCookie, TbItem.class));

        //删除cookie中的购物车
        CookieUtils.deleteCookie(request,response,CART_COOKIE);

        request.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
