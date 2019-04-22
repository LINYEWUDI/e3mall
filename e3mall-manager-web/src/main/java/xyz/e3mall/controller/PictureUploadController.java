package xyz.e3mall.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xyz.e3mall.utils.FastDFSClient;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureUploadController {

    //解决硬编码
    @Value("${serverURL}")
    String serverURL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public Map uploadPicture(MultipartFile uploadFile){
        try {
            //获取上传文件的文件名
            String filename = uploadFile.getOriginalFilename();
            //获取拓展名
            String extName = filename.substring(filename.lastIndexOf(".") + 1);
            //使用fastDFS工具类进行上传
            FastDFSClient fastDFSClient = new FastDFSClient("C:\\Users\\28937\\IdeaProjects\\e3mall-web\\src\\main\\resource\\fastDFSconfig\\fastDFS.conf");
            //在fastDFS中保存的地址
            String addr = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);

            String url = serverURL+addr;
            Map map = new HashMap<String,Object>();
            map.put("error",0);
            map.put("url",url);

            //返回json
            return map;

        } catch (Exception e) {
            Map map = new HashMap<String,Object>();
            map.put("error",0);
            map.put("message", "图片上传失败");

            //返回json
            return map;
        }


    }
}
