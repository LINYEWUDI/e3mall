package xyz.e3mall.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Hello world!
 *
 */
public class EasyUiPojo implements Serializable
{
    int total;
    List list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "EasyUiPojo{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
