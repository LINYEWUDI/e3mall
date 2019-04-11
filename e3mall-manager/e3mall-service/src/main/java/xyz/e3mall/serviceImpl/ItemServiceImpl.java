package xyz.e3mall.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.e3mall.ItemService;
import xyz.e3mall.domain.E3Result;
import xyz.e3mall.mapper.TbItemDescMapper;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.e3mall.pojo.EasyUiPojo;
import xyz.e3mall.pojo.TbItem;
import xyz.e3mall.pojo.TbItemDesc;
import xyz.e3mall.pojo.TbItemExample;
import xyz.e3mall.utils.IDUtils;

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

    @Override
    public TbItem getItemById(long id) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        return tbItem;
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
    public E3Result addItem(TbItem tbItem, String desc ,String imgAddr) {

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
