package xyz.linye.fastDFS;

import org.csource.fastdfs.*;
import org.junit.Test;
import xyz.e3mall.common.utils.FastDFSClient;


public class fastDFSTest {

    @Test
    public void fastDFStest() {
        try {
            //使用全局变量加载配置文件
            ClientGlobal.init("C:\\Users\\28937\\IdeaProjects\\e3mall-web\\src\\main\\resource\\fastDFSconfig\\fastDFS.conf");
            //创建注册客户端
            TrackerClient trackerClient = new TrackerClient();
            //返回注册的服务器对象
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建储存服务器对象
            StorageServer storageServer = null;

            //最终得到储存客户端对象，此对象可以进行储存
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);

            String[] jpgs = storageClient.upload_file("D:\\32期\\【阶段16】宜立方商城项目\\01e3商城工具\\广告图片\\9a80e2d06170b6bb01046233ede701b3.jpg", "jpg", null);

            for (String jpg : jpgs) {
                System.out.println(jpg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fastDFSUtilstest() {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("C:\\Users\\28937\\IdeaProjects\\e3mall-web\\src\\main\\resource\\fastDFSconfig\\fastDFS.conf");

            String str = fastDFSClient.uploadFile("D:\\32期\\【阶段16】宜立方商城项目\\01e3商城工具\\广告图片\\1946ceef1ea90c932e1f7c8bb631a3fa.jpg");

            System.out.println(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
