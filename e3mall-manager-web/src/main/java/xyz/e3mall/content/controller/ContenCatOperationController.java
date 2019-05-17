package xyz.e3mall.content.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.content.service.ContentCatListService;

import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.domain.EasyUiItemCatVo;

import java.util.List;

@Controller
public class ContenCatOperationController {

    @Autowired
    ContentCatListService contentCatListService;

    @RequestMapping("content/category/{parentId}")
    @ResponseBody
    public List<EasyUiItemCatVo> getContentCatList(@PathVariable("parentId") Long parentId){
        List<EasyUiItemCatVo> contentCatList = contentCatListService.getContentCatList(parentId);

        return contentCatList;

    }

    @RequestMapping(value = "content/category" ,method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContentCat(Long parentId,String name){

        E3Result e3Result = contentCatListService.addContentCat(parentId, name);

        return e3Result;

    }

    @RequestMapping(value = "content/category" ,method = RequestMethod.PUT)
    @ResponseBody
    public E3Result updateContentCat(Long id,String name){//修改商品的id和name

        E3Result e3Result = contentCatListService.updateContentCat(id, name);

        return e3Result;

    }

    @RequestMapping(value = "content/category" ,method = RequestMethod.DELETE)
    @ResponseBody
    public E3Result deleteContentCat(Long id){//删除商品的id

        E3Result e3Result = contentCatListService.deleteContentCat(id);

        return e3Result;

    }
}
