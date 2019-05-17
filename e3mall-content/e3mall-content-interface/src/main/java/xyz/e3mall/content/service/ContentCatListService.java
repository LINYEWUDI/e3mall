package xyz.e3mall.content.service;

import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.domain.EasyUiItemCatVo;

import java.util.List;

public interface ContentCatListService {

    //拿到所有内容目录content_category
    public List<EasyUiItemCatVo> getContentCatList(Long parentId);

    //新增内容分类节点
    public E3Result addContentCat(Long parentId, String name);

    //修改内容分类(重命名)
    public E3Result updateContentCat(Long id, String name);

    //删除内容分类
    public E3Result deleteContentCat(Long id);

}
