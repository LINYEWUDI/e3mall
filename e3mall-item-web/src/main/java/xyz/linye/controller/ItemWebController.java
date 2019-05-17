package xyz.linye.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.manager.ItemService;
import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.manager.pojo.TbItemDesc;
import xyz.linye.domain.Item;

@Controller
public class ItemWebController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/item/{id}")
    public @ResponseBody
    TbItem getItemById(@PathVariable("id") int id){
        //获取商品详情
        TbItem tbItem = itemService.getItemById(id);

        //获取商品描述信息
        TbItemDesc itemDescById = itemService.getItemDescById(id);

        //创建更新之后的对象
        Item item = new Item(tbItem);

        item.setTbItemDesc(itemDescById);
        //返回json
        return item;
    }


}
