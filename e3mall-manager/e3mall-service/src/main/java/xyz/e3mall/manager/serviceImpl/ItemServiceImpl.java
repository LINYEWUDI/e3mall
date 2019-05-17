package xyz.e3mall.manager.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import xyz.e3mall.manager.ItemService;
import xyz.e3mall.common.JedisUtils.JedisClient;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbItemDesc;
import xyz.e3mall.mapper.TbItemDescMapper;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.e3mall.common.domain.EasyUiPojo;
import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.manager.pojo.TbItemExample;
import xyz.e3mall.common.utils.IDUtils;
import xyz.e3mall.common.utils.JsonUtils;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    TbItemMapper tbItemMapper;

    @Autowired
    TbItemDescMapper tbItemDescMapper;

    @Autowired
    JmsTemplate jmsTemplate;

    @Resource(name = "topicDestination")
    Destination topicDestination;

    @Autowired
    JedisClient jedisClient;

    //从配置文件中取出
    @Value("${ITEM_INFO_PRE}")
    String ITEM_INFO_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")
    int ITEM_CACHE_EXPIRE;



    @Override
    public TbItemDesc getItemDescById(long id) {
        try {
            //先从缓存中进行查找
            String descJson = jedisClient.get(ITEM_INFO_PRE + ":" + id + ":DESC");
            if(StringUtils.isNotBlank(descJson)){
                //转换成实体
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(descJson, TbItemDesc.class);

                return tbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //没有缓存时
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);

        try {
            //放入缓存中
            jedisClient.set(ITEM_INFO_PRE+":"+":DESC",JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间
            jedisClient.expire(ITEM_INFO_PRE+":"+":DESC",ITEM_CACHE_EXPIRE);
            return tbItemDesc;

        }catch (Exception e){
            e.printStackTrace();
        }

         return null;
    }

    @Override
    public TbItem getItemById(long id) {
        //先从缓存中取出
        try {
            String itemJson = jedisClient.get(ITEM_INFO_PRE + ":" + id + ":BASE");

            if(StringUtils.isNotBlank(itemJson)){
                TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);

        try {
            //放入缓存
            if(tbItem != null){
                String toJson = JsonUtils.objectToJson(tbItem);
                jedisClient.set(ITEM_INFO_PRE+":"+id+":BASE",toJson);

                //设置过期时间
                jedisClient.expire(ITEM_INFO_PRE+":"+id+":BASE",ITEM_CACHE_EXPIRE);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public EasyUiPojo getItemByPage(int startPage, int rows) {

        //进行分页
        PageHelper.startPage(startPage,rows);

        //查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);

        //拿到总共的商品数量
        PageInfo pageInfo = new PageInfo(list);
        int size = pageInfo.getSize();

        EasyUiPojo pojo = new EasyUiPojo();
        pojo.setTotal(size);
        pojo.setList(list);

        return pojo;
    }


    @Override
    public E3Result addItem(TbItem tbItem, String desc , String imgAddr) {

        //补充实体
        //使用id生成器得到id
        long itemId = IDUtils.genItemId();
        //添加id
        tbItem.setId(itemId);
        //添加商品父节点，写死cid
        tbItem.setCid((long)560);
        //商品状态，1-正常，2-下架，3-删除
        tbItem.setStatus((byte)1);
        //设置上架时间和创建商品时间
        tbItem.setUpdated(new Date());
        tbItem.setCreated(new Date());
        //设置商品照片
        tbItem.setImage(imgAddr);




        //创建商品描述详情对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        //设置id
        tbItemDesc.setItemId(itemId);
        //填入商品详情
        tbItemDesc.setItemDesc(desc);
        //设置上架时间和创建商品时间
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());



        tbItemMapper.insert(tbItem);
        tbItemDescMapper.insert(tbItemDesc);

        E3Result e3Result = E3Result.ok("添加成功!");

        //发送消息给消息队列
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(tbItem.getId()+"");
                return textMessage;
            }
        });

        return e3Result;
    }

    @Override
    public Map updateItem(TbItem tbItem) {

        tbItem.setTitle("IPHONEXXXXXXXXXXXXXXXXXXXXX");

        int flag = tbItemMapper.updateByPrimaryKey(tbItem);
        Map map = new HashMap();

        if(flag != 0){
            map.put("error","1");
            map.put("message","修改成功！");
            return map;
        }else {
            map.put("error","0");
            map.put("message","修改失败！");
            return map;
        }
    }

    @Override
    public Map deleteItem(Integer itemId) {
        int flag = tbItemMapper.deleteByPrimaryKey(itemId);

        Map map = new HashMap();

        if(flag != 0){
            map.put("error","1");
            map.put("message","删除成功！");
            return map;
        }else {
            map.put("error","0");
            map.put("message","删除失败！");
            return map;
        }
    }


}
