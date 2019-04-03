package xyz.e3mall.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.e3mall.ItemService;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.e3mall.pojo.EasyUiPojo;
import xyz.e3mall.pojo.TbItem;
import xyz.e3mall.pojo.TbItemExample;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    TbItemMapper tbItemMapper;

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
}
