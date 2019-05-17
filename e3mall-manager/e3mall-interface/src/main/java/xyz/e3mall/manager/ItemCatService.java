package xyz.e3mall.manager;

import xyz.e3mall.common.domain.EasyUiItemCatVo;

import java.util.List;

public interface ItemCatService {

    public List<EasyUiItemCatVo> getItemCatList(long id);
}
