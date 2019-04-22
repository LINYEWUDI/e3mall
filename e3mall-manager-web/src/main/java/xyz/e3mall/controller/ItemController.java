package xyz.e3mall.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.ItemService;
import xyz.e3mall.domain.EasyUiPojo;
import xyz.e3mall.pojo.TbItem;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/item/{id}")
    public @ResponseBody
    TbItem getItemById(@PathVariable("id") int id){
        TbItem item = itemService.getItemById(id);
        //返回json
        return item;
    };


    //需要设置起始页数和每页显示的条数
    @RequestMapping("/item/list")
    public @ResponseBody
    EasyUiPojo getItemByPage(int page,int rows){
        EasyUiPojo itemByPage = itemService.getItemByPage(page, rows);
        //返回json
        return itemByPage;
    };




}
