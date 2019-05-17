package xyz.linye.cart.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.utils.CookieUtils;
import xyz.e3mall.manager.pojo.TbUser;
import xyz.e3mall.sso.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Value("${CART_COOKIE}")
    String CART_COOKIE;

    //检验用户是否登录
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从cookie中取token
        String token = CookieUtils.getCookieValue(request, "COOKIE_TOKEN_ID", true);

        //若cookie没有token，则用户未登录
        if(StringUtils.isBlank(token)){
            return true;
        }

        //若有token，则在reids中获取该用户
        E3Result e3Result = userService.getTokenUser("SESSION_ID:"+token);
        if(e3Result.getStatus() != 200){
            //若不存在
            return true;
        }
        //用户已登录的情况
        //获取到token对应的用户
        TbUser user = (TbUser) e3Result.getData();
        //放进域中
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
