package xyz.pojo;

import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.manager.pojo.TbOrder;
import xyz.e3mall.manager.pojo.TbOrderItem;
import xyz.e3mall.manager.pojo.TbOrderShipping;

import java.util.List;

public class TbOrderInfo extends TbOrder {

    private List<TbOrderItem> orderItemList;
    private TbOrderShipping tbOrderShipping;

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public TbOrderShipping getTbOrderShipping() {
        return tbOrderShipping;
    }

    public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
        this.tbOrderShipping = tbOrderShipping;
    }

    @Override
    public String toString() {
        return "TbOrderInfo{" +
                "orderItemList=" + orderItemList +
                ", tbOrderShipping=" + tbOrderShipping +
                '}';
    }


}
