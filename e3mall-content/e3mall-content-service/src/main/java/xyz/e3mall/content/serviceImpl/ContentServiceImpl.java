package xyz.e3mall.content.serviceImpl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.e3mall.content.service.ContentSerice;
import xyz.e3mall.domain.E3Result;
import xyz.e3mall.domain.EasyUiPojo;
import xyz.e3mall.mapper.TbContentMapper;
import xyz.e3mall.pojo.TbContent;
import xyz.e3mall.pojo.TbContentExample;

import java.util.List;


@Service
public class ContentServiceImpl implements ContentSerice {

    @Autowired
    TbContentMapper tbContentMapper;

    @Override
    public EasyUiPojo getContentList(Long startPage, Long rows, Long categoryId) {
        int page = startPage.intValue();
        int size = rows.intValue();
        //进行分页
        PageHelper.startPage(page,size);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //拿到指定分类下的所有内容列表
        List<TbContent> tbContentList = tbContentMapper.selectByExample(example);

        //拿到总的条数
        int allCount = tbContentMapper.countByExample(example);

        EasyUiPojo pojo = new EasyUiPojo();
        pojo.setTotal(allCount);
        pojo.setList(tbContentList);

        return pojo;
    }

    //添加内容
    @Override
    public E3Result saveContent(TbContent tbContent) {
        tbContentMapper.insert(tbContent);
        return E3Result.ok("添加成功！");
    }

    //根据分类id取到内容，展示给前台
    @Override
    public List<TbContent> getContentForPortal(Long categoryId) {
        //进行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);

        List<TbContent> contentList = tbContentMapper.selectByExample(example);

        return contentList;
    }

}
