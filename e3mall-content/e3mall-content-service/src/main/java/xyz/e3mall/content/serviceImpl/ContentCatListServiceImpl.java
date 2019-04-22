package xyz.e3mall.content.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.e3mall.content.service.ContentCatListService;
import xyz.e3mall.domain.E3Result;
import xyz.e3mall.domain.EasyUiItemCatVo;
import xyz.e3mall.mapper.TbContentCategoryMapper;
import xyz.e3mall.pojo.TbContentCategory;
import xyz.e3mall.pojo.TbContentCategoryExample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCatListServiceImpl implements ContentCatListService {

    @Autowired
    TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUiItemCatVo> getContentCatList(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //返回所有的内容目录
        List<TbContentCategory> contentCategoryList = tbContentCategoryMapper.selectByExample(example);

        //创建集合
        List<EasyUiItemCatVo> list = new ArrayList<EasyUiItemCatVo>();

        for (TbContentCategory tbContentCategory : contentCategoryList) {
            //创建返回的实体
            EasyUiItemCatVo vo = new EasyUiItemCatVo();

            vo.setId(tbContentCategory.getId());
            vo.setText(tbContentCategory.getName());
            vo.setStatus(tbContentCategory.getIsParent() ? "closed" : "open");

            list.add(vo);
        }
        return list;
    }

    @Override
    public E3Result addContentCat(Long parentId, String name) {
        //创建表实体，封装对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        //父节点id
        tbContentCategory.setParentId(parentId);
        //分类名字
        tbContentCategory.setName(name);
        //添加和修改日期
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //新添加的节点一定是叶子节点
        tbContentCategory.setIsParent(false);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);

        //进行插入
        tbContentCategoryMapper.insert(tbContentCategory);

        //判断父节点的isParent，如果之前为叶子节点，则修改为父节点
        TbContentCategory tbContentCategoryParent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!tbContentCategoryParent.getIsParent()) {
            tbContentCategoryParent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(tbContentCategoryParent);
        }

        return E3Result.ok("添加成功！");
    }

    @Override
    public E3Result updateContentCat(Long id, String name) {
        //通过id查询内容分类的信息
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        if (tbContentCategory != null) {
            //修改分类的名字
            tbContentCategory.setName(name);
            int i = tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);

            if (i > 0) {
                return E3Result.ok("修改成功！");
            } else {
                E3Result e3Result = new E3Result();
                e3Result.setStatus(500);
                e3Result.setMsg("this is an error!");
                e3Result.setData("修改失败");
                return e3Result;
            }
        } else {
            E3Result e3Result = new E3Result();
            e3Result.setStatus(500);
            e3Result.setMsg("this is an error!");
            e3Result.setData("用户名不存在！");
            return e3Result;
        }


    }

    @Override
    public E3Result deleteContentCat(Long id) {
        //获取删除节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        //删除节点不为空
        if (tbContentCategory != null) {
            //取到删除节点的父节点
            Long parentId = tbContentCategory.getParentId();
            //查询该父节点下还有没有子节点
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            //查询删除节点的父节点下的所有子节点
            List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(example);

            //如果该父节点只有删除节点这一个子节点时，修改isParent属性为false(变成子节点)
            if(tbContentCategoryList.size() < 2){
                TbContentCategory parenttbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
                parenttbContentCategory.setIsParent(false);
                //修改到数据库中
                tbContentCategoryMapper.updateByPrimaryKey(parenttbContentCategory);
            }

            tbContentCategoryMapper.deleteByPrimaryKey(id);

            return E3Result.ok("删除成功！");
        }


        return E3Result.ok("删除的商品不存在！");
    }



}
