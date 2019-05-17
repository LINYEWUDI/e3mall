package xyz.linye.domain;

import xyz.e3mall.manager.pojo.TbItem;
import xyz.e3mall.manager.pojo.TbItemDesc;

public class Item extends TbItem {

    TbItemDesc tbItemDesc;

    public TbItemDesc getTbItemDesc() {
        return tbItemDesc;
    }

    public void setTbItemDesc(TbItemDesc tbItemDesc) {
        this.tbItemDesc = tbItemDesc;
    }

    public Item(TbItem tbItem) {
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setId(tbItem.getId());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setStatus(tbItem.getStatus());
        this.setTitle(tbItem.getTitle());
        this.setUpdated(tbItem.getUpdated());
    }


}
