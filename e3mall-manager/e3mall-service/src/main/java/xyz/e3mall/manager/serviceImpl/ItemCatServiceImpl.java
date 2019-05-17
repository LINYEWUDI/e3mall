package xyz.e3mall.manager.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.e3mall.manager.ItemCatService;
import xyz.e3mall.common.domain.EasyUiItemCatVo;
import xyz.e3mall.mapper.TbItemCatMapper;
import xyz.e3mall.manager.pojo.TbItemCat;
import xyz.e3mall.manager.pojo.TbItemCatExample;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    TbItemCatMapper tbItemCatMapper;

    //通过mapper对象访问数据库
    @Override
    public List<EasyUiItemCatVo> getItemCatList(long id) {//接收controller层传输过来的id
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        //通过parentId拿到对应商品分类
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);

        //创建返回前端json的数组
        List<EasyUiItemCatVo> voList = new ArrayList<EasyUiItemCatVo>();

        for (TbItemCat tbItemCat : list){
            //创建Vo类对象，完成实体的注入
            EasyUiItemCatVo vo = new EasyUiItemCatVo();
            vo.setId(tbItemCat.getId());
            vo.setText(tbItemCat.getName());
            vo.setStatus(tbItemCat.getIsParent()?"closed":"opened");
            //放入数组中
            voList.add(vo);
        }

        return voList;
    }
}
