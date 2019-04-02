package xyz.e3mall;
import xyz.e3mall.pojo.EasyUiPojo;
import xyz.e3mall.pojo.TbItem;

/**
 * 接口
 */
public interface ItemService {

    //通过id查找商品
    public TbItem getItemById(long id);


    //分页显示所有的商品
    public EasyUiPojo getItemByPage(int startPage, int rows);

}
