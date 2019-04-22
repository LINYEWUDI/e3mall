package xyz.e3mall.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.content.service.ContentSerice;
import xyz.e3mall.domain.E3Result;
import xyz.e3mall.domain.EasyUiPojo;
import xyz.e3mall.pojo.TbContent;

@Controller
public class ContentController {

    @Autowired
    ContentSerice contentSerice;

    @RequestMapping(value = "content",method = RequestMethod.GET)
    @ResponseBody
    public EasyUiPojo getContentList(Long startPage, Long rows, Long categoryId){
        EasyUiPojo pojo = contentSerice.getContentList(startPage, rows, categoryId);
        return pojo;
    }

    @RequestMapping(value = "content",method = RequestMethod.POST)
    @ResponseBody
    public E3Result saveContent(){
        TbContent tbContent = new TbContent();
        tbContent.setCategoryId((long)89);
        tbContent.setTitle("终于成功了");
        E3Result e3Result = contentSerice.saveContent(tbContent);
        return e3Result;
    }
}
