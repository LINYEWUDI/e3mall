package xyz.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xyz.e3mall.ItemService;
import xyz.e3mall.domain.E3Result;
import xyz.e3mall.pojo.TbItem;
import xyz.e3mall.utils.FastDFSClient;

import javax.swing.*;
import java.util.Map;

@Controller
public class ItemOperationController {

    @Autowired
    ItemService itemService;

    @Value("${serverURL}")
    String serverURL;


    @RequestMapping(value = "item", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem tbItem, String desc, MultipartFile uploadFile) {

        //图片在fastDFS中地址
        String imgAddr = null;

        //拿到上传文件的文件名
        String filename = uploadFile.getOriginalFilename();

        //拿到上传文件名的拓展名
        String extName = filename.substring(filename.lastIndexOf(".") + 1);

        try {
            //使用fastDFS工具类进行上传
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFSconfig/fastDFS.conf");
            //图片服务器中的地址  例：多少组/名字.后缀名
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            imgAddr = serverURL + path;

        } catch (Exception e) {
            e.printStackTrace();
        }

        E3Result e3Result = itemService.addItem(tbItem, desc, imgAddr);
        return e3Result;
    }

    /**
     * 模拟前后端分离，在此简化为前端传入修改商品的id，修改商品在后端修改，实际项目由前端传入实体，然后全部修改
     */
    @RequestMapping(value = "item", method = RequestMethod.PUT)
    @ResponseBody
    public Map updateItem(Integer itemId) {
        //通过前端获取的itemId取到商品
        TbItem item = itemService.getItemById(itemId);
        Map map = itemService.updateItem(item);
        System.out.println(map.toString());
        return map;
    }


    @RequestMapping(value = "item", method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteItem(Integer itemId) {
        Map map = itemService.deleteItem(itemId);
        return map;
    }

}

