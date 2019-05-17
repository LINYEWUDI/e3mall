package xyz.e3mall.content.service;

import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.domain.EasyUiPojo;
import xyz.e3mall.manager.pojo.TbContent;

import java.util.List;

public interface ContentSerice {

    //分页查询所有的内容
    public EasyUiPojo getContentList(Long startPage, Long rows, Long categoryId);

    //添加新的内容
    public E3Result saveContent(TbContent tbContent);

    //通过categoryId查询内容，展示给前台
    public List<TbContent> getContentForPortal(Long categoryId);

}
