package xyz.e3mall.sso;

import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbUser;

public interface UserService {

     //数据校验
     E3Result checkData(String param, Integer type);
     //注册
     E3Result registerUser(TbUser tbUser);
     //登录
     E3Result loginUser(String username,String password);
     //通过token获取信息
     E3Result getTokenUser(String token);
}
