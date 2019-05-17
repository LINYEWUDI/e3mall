package xyz.e3mall.content.messageimpl;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.e3mall.common.JedisUtils.JedisClient;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.e3mall.manager.pojo.TbItem;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//添加商品时进行同步
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    TbItemMapper tbItemMapper;

    @Autowired
    JedisClient jedisClient;

    @Override
    public void onMessage(Message message) {
        try {
            //强转
            TextMessage textMessage = (TextMessage)message;
            String text = textMessage.getText();
            //转换成Long类型
            Long itemId = new Long(text);
            //获取商品id
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
            //写入redis
            jedisClient.hset("ITEM_LIST","itemId",itemId+"");

            System.out.println(tbItem);

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
