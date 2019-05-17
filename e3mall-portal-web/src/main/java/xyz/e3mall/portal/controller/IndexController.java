package xyz.e3mall.portal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.content.service.ContentSerice;
import xyz.e3mall.manager.pojo.TbContent;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ContentSerice contentSerice;

    @Value("${CATEGORY_ID}")
    private Long CATEGORY_ID;

    @RequestMapping(value = "showIndex",method = RequestMethod.GET)
    @ResponseBody
    public List<TbContent> getContentForPortal(){
        List<TbContent> contentList = contentSerice.getContentForPortal(CATEGORY_ID);
        return contentList;

    }
}
