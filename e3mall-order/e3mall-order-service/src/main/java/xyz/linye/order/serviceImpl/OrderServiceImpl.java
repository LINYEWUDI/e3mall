package xyz.linye.order.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;
import xyz.e3mall.OrderService;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbOrderItem;
import xyz.e3mall.manager.pojo.TbOrderShipping;
import xyz.e3mall.mapper.TbOrderItemMapper;
import xyz.e3mall.mapper.TbOrderMapper;
import xyz.e3mall.mapper.TbOrderShippingMapper;
import xyz.pojo.TbOrderInfo;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

     @Autowired
     TbOrderMapper tbOrderMapper;

     @Autowired
     TbOrderItemMapper tbOrderItemMapper;

     @Autowired
     TbOrderShippingMapper tbOrderShippingMapper;

     @Autowired
     JedisCluster jedisCluster;

     @Value("${ORDER_ID_NAME}")
     String ORDER_ID_NAME;//订单号id名字

     @Value("${ORDER_ID_STARTNUM}")
     String ORDER_ID_STARTNUM;//订单号起始数字

     @Value("${ORDER_ITEM_ID_NAME}")
     String ORDER_ITEM_ID_NAME;//订单明细表id的名字

     public E3Result createOrder(TbOrderInfo tbOrderInfo){
          //首先判断redis中是否窜在该主键，若存在一直自增，不存在则创建
          if(!jedisCluster.exists(ORDER_ID_NAME)){
               jedisCluster.set(ORDER_ID_NAME,ORDER_ID_STARTNUM);
          }
          //生成订单号
          String orderId = jedisCluster.incr(ORDER_ID_NAME).toString();

          /*
               补全属性
           */
          tbOrderInfo.setOrderId(orderId);
          //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
          tbOrderInfo.setStatus(1);
          //确定时间
          tbOrderInfo.setUpdateTime(new Date());
          tbOrderInfo.setCreateTime(new Date());

          //插入订单表中
          tbOrderMapper.insert(tbOrderInfo);

          //获取订单明细表
          List<TbOrderItem> orderItemList = tbOrderInfo.getOrderItemList();
          for (TbOrderItem tbOrderItem : orderItemList) {
               //直接生成id,因为不需要展示给用户，不需要设置初始值
               String orderItemId = jedisCluster.incr(ORDER_ITEM_ID_NAME).toString();
               //补全数据
               tbOrderItem.setId(orderItemId);
               tbOrderItem.setOrderId(orderId);
               //进行插入
               tbOrderItemMapper.insert(tbOrderItem);
          }

          //获取收货信息表
          TbOrderShipping tbOrderShipping = tbOrderInfo.getTbOrderShipping();
          tbOrderShipping.setOrderId(orderId);
          tbOrderShipping.setCreated(new Date());
          tbOrderShipping.setUpdated(new Date());

          return E3Result.ok(orderId);

     }
}
