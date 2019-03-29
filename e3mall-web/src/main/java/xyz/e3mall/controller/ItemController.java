package xyz.e3mall.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.ItemService;
import xyz.e3mall.pojo.TbItem;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/getItem/{id}")
    public @ResponseBody
    TbItem getItemById(@PathVariable("id") int id){
        System.out.println("iii");
        TbItem item = itemService.getItemById(id);
        //返回json
        return item;
    };


}
