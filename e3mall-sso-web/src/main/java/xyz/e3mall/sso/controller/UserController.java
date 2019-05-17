package xyz.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.sso.UserService;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbUser;
import xyz.e3mall.common.utils.CookieUtils;
import xyz.e3mall.common.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public E3Result checkData(@PathVariable("param") String param, @PathVariable("type") Integer type){
        E3Result e3Result = userService.checkData(param, type);

        return e3Result;
    }

    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result registerUser(TbUser tbUser){
        E3Result e3Result = userService.registerUser(tbUser);

        return e3Result;
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result loginUser(String username, String password, HttpServletResponse response, HttpServletRequest request){
        E3Result e3Result = userService.loginUser(username,password);

        //从返回值中获取token
        String token = (String)e3Result.getData();

        //写入cookie，cookie要跨域
        CookieUtils.setCookie(request,response,"COOKIE_TOKEN_ID",token);

        return e3Result;
    }


    /*
     *通过token获取
     */
    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET)
    @ResponseBody
    public E3Result getTokenUser(@PathVariable("token") String token){
        //token即伪sessionid
        E3Result e3Result = userService.getTokenUser(token);

        return e3Result;
    }

    /*
     * 模拟jsonp调用端口获取数据
     * 当JS跨域请求时，服务器是可以接收到JS的请求，
     * 但是跨域的服务器响应的数据会被浏览器拒收，所以采用jsonp(漏洞)
     *              jsonp原理：
     *  JS跨域请求的响应的json数据会被浏览器绝收，但是跨域请求返回的js代码可以接收(类似jqery),
     *  jquery发送的ajax中的dataType属性修改为jsonp后，
     *  会自动添加一个方法Function xxx(data)，在该方法中会取到参数data，并进行业务处理
     *  同时发送的请求中会默认携带参数callback=xxx，
     *  所以我们在服务器端只需要接收到该方法名即xxx，并通过拼接字符串的方式，
     *  拼接成   xxx(data); 也就是js的方法调用   的形式返回， 返回给前端后即自动执行该方法
     *
     *  综上：变相跨域获取到了数据
     *
     *
     */
    @RequestMapping(value = "/user/jsonp/{token}",method = RequestMethod.GET)
    @ResponseBody
    public String getJsonpUser(@PathVariable("token") String token,String callback){
        //token即伪sessionid
        E3Result e3Result = userService.getTokenUser(token);

        if(StringUtils.isNotBlank(callback)){
            return callback+"("+ JsonUtils.objectToJson(e3Result)+");";
        }

        return JsonUtils.objectToJson(e3Result);
    }
}
