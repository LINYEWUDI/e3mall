package xyz.e3mall;

import xyz.e3mall.common.domain.E3Result;
import xyz.pojo.TbOrderInfo;

public interface OrderService {

    public E3Result createOrder(TbOrderInfo tbOrderInfo);
}
