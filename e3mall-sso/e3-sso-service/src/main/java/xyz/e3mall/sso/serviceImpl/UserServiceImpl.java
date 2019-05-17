package xyz.e3mall.sso.serviceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import xyz.e3mall.common.JedisUtils.JedisClient;
import xyz.e3mall.sso.UserService;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbUser;
import xyz.e3mall.manager.pojo.TbUserExample;
import xyz.e3mall.mapper.TbUserMapper;
import xyz.e3mall.common.utils.JsonUtils;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
    /**
     * 校验注册时用户名或者电话或者邮箱是否重复
     * @param param
     * @param type
     */
    @Autowired
    TbUserMapper tbUserMapper;

    @Autowired
    JedisClient jedisClient;

    @Value("${EXPIRE_TIME}")
    int expireTime;

    @Override
    public E3Result checkData(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        //判断参数是否为空
        if(param!=null && type!=null && !"".equals(param) && !"".equals(type)){
            //电话校验
            if(type == 1){
                criteria.andPhoneEqualTo(param);
            }else if (type == 2){
                criteria.andUsernameEqualTo(param);
            }else if (type == 3){
                criteria.andEmailEqualTo(param);
            }

            List<TbUser> userList = tbUserMapper.selectByExample(example);

            if(userList == null || userList.size() == 0){
                return E3Result.ok();
            }else {
                return E3Result.build(400,"用户信息已存在");

            }

        }else {
            return new E3Result().build(400, "非法的参数");
        }


    }

    @Override
    public E3Result registerUser(TbUser tbUser) {
        //进行有效性判断
        if("".equals(tbUser.getUsername()) ||
                "".equals(tbUser.getPhone()) ||
                        "".equals(tbUser.getEmail())){

            return E3Result.build(400,"东西都没得，玩济公玩");
        }

        if("400".equals(checkData(tbUser.getPhone(),1).getData())){
            return checkData(tbUser.getPhone(),1);
        }

        if("400".equals(checkData(tbUser.getUsername(),2).getData())){
            return checkData(tbUser.getUsername(),2);
        }

        if("400".equals(checkData(tbUser.getEmail(),3).getData())){
            return checkData(tbUser.getEmail(),3);
        }

        if(StringUtils.isBlank(tbUser.getPassword())){
            return E3Result.build(400,"密码没填玩济公玩");

        }

        //username、password、phone、email
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());

        //md5加密
        String md5Pwd = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());

        tbUser.setPassword(md5Pwd);

        int num = tbUserMapper.insert(tbUser);

        if(num == 0){
            return E3Result.build(400,"注册失败！");
        }

        return E3Result.ok();
    }

    /**
     * 单点登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public E3Result loginUser(String username, String password) {

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return E3Result.build(400,"用户名或密码不能为空！");
        }
        //先进行比对信息
        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUserList = tbUserMapper.selectByExample(example);

        if(tbUserList == null || tbUserList.size() == 0){
            return E3Result.build(400,"用户名或密码错误！");
        }

        if(!tbUserList.get(0).getPassword().equals(md5Pwd)){
            return E3Result.build(400,"用户名或密码错误！");
        }

        //使用UUID生成token,模拟sessioid
        String token = UUID.randomUUID().toString();

        //写入redis中
        jedisClient.set("SESSION_ID:"+token, JsonUtils.objectToJson(tbUserList.get(0)));

        //设置session过期时间
        jedisClient.expire("SESSION_ID:"+token,expireTime);


        return E3Result.ok(token);
    }

    @Override
    public E3Result getTokenUser(String token) {
        //通过redis查询user
        String token_user_json = jedisClient.get(token);

        if(StringUtils.isBlank(token_user_json)){
            return E3Result.build(400,"身份已过期");
        }

        //查询到信息，说明还登录
        jedisClient.expire("SESSION_ID:"+token,expireTime);


       return E3Result.ok(JsonUtils.jsonToPojo(token_user_json,TbUser.class));
    }
}
