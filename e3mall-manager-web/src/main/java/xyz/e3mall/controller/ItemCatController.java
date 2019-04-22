package xyz.e3mall.controller;

import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.e3mall.ItemCatService;
import xyz.e3mall.domain.EasyUiItemCatVo;

import java.util.List;

@Controller
public class ItemCatController {

    @Autowired
    ItemCatService itemCatService;

    @RequestMapping("item/cat/{parentId}")
    @ResponseBody
    public List<EasyUiItemCatVo> getItemCatList(@PathVariable("parentId") long parentId){
        List<EasyUiItemCatVo> itemCatList = itemCatService.getItemCatList(parentId);
        return itemCatList;

    }
}
