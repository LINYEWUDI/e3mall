package xyz.e3mall.content.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.manager.ItemService;
import xyz.e3mall.common.domain.EasyUiPojo;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;


    //需要设置起始页数和每页显示的条数
    @RequestMapping("/item/list")
    public @ResponseBody
    EasyUiPojo getItemByPage(int page,int rows){
        EasyUiPojo itemByPage = itemService.getItemByPage(page, rows);
        //返回json
        return itemByPage;
    };




}
