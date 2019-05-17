package xyz.e3mall.content.serviceImpl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.e3mall.common.JedisUtils.JedisClient;
import xyz.e3mall.content.service.ContentSerice;
import xyz.e3mall.common.domain.E3Result;
import xyz.e3mall.common.domain.EasyUiPojo;
import xyz.e3mall.mapper.TbContentMapper;
import xyz.e3mall.manager.pojo.TbContent;
import xyz.e3mall.manager.pojo.TbContentExample;
import xyz.e3mall.common.utils.JsonUtils;

import java.util.List;


@Service
public class ContentServiceImpl implements ContentSerice {

    @Autowired
    TbContentMapper tbContentMapper;

    //spring容器中配置的jedisClientCluster
    @Autowired
    JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    String CONTENT_LIST;

    @Override
    public EasyUiPojo getContentList(Long startPage, Long rows, Long categoryId) {


        int page = startPage.intValue();
        int size = rows.intValue();
        //进行分页
        PageHelper.startPage(page,size);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //拿到指定分类下的所有内容列表
        List<TbContent> tbContentList = tbContentMapper.selectByExample(example);

        //拿到总的条数
        int allCount = tbContentMapper.countByExample(example);

        EasyUiPojo pojo = new EasyUiPojo();
        pojo.setTotal(allCount);
        pojo.setList(tbContentList);

        return pojo;
    }

    //添加内容
    @Override
    public E3Result saveContent(TbContent tbContent) {
        tbContentMapper.insert(tbContent);
        try {
            //缓存同步
            jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId() + "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return E3Result.ok("添加成功！");
    }

    //根据分类id取到内容，展示给前台
    @Override
    public List<TbContent> getContentForPortal(Long categoryId) {
        //先从缓存中取
        try {
            //如果在缓存中存在，取出来则为json格式的字符串
            String contentListJson = jedisClient.hget(CONTENT_LIST, categoryId + "");

            if(contentListJson != null){
                //存在时，将json转换成list,第二个参数为list集合的泛型
                List<TbContent> contentList = JsonUtils.jsonToList(contentListJson, TbContent.class);
                return contentList;
            }
        }catch (Exception e){

        }

        //进行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);

        List<TbContent> contentList = tbContentMapper.selectByExample(example);

        try {
            //添加缓存
            jedisClient.hset(CONTENT_LIST,categoryId+"",JsonUtils.objectToJson(contentList));
        }catch (Exception e){

        }

        return contentList;
    }

}
