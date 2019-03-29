package xyz.e3mall.serviceImpl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.e3mall.ItemService;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.e3mall.pojo.TbItem;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    TbItemMapper tbItemMapper;

    @Override
    public TbItem getItemById(long id) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        return tbItem;
    }
}
