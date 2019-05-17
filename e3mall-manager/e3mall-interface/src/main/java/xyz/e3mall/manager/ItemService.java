package xyz.e3mall.manager;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.manager.pojo.TbItemDesc;
import xyz.e3mall.common.domain.EasyUiPojo;
import xyz.e3mall.manager.pojo.TbItem;

import java.util.Map;

/**
 * 接口
 */
public interface ItemService {

    //通过id查找商品
    public TbItem getItemById(long id);

    //通过id查找商品
    public TbItemDesc getItemDescById(long id);


    //分页显示所有的商品
    public EasyUiPojo getItemByPage(int startPage, int rows);

    //分页显示所有的商品
    public E3Result addItem(TbItem tbItem, String desc , String imgAddr);

    //修改商品
    public Map updateItem(TbItem tbItem);

    //删除商品
    public Map deleteItem(Integer itemId);
}
