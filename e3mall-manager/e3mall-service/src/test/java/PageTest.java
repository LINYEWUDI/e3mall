import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xyz.e3mall.mapper.TbItemMapper;
import xyz.e3mall.pojo.TbItem;
import xyz.e3mall.pojo.TbItemExample;

import java.util.List;

public class PageTest {

    @Test
    public void pageTest(){
        //创建spring容器
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        //拿到mapper对象
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        //从第几页开始，每页展示多少个
        PageHelper.startPage(1,10);
        //使用pageHelper
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //拿到分页后的参数
        PageInfo pageInfo = new PageInfo(list);

        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        int size = pageInfo.getSize();

        System.out.println("total:"+total);
        System.out.println("pages:"+pages);
        System.out.println("size:"+size);
    }
}
