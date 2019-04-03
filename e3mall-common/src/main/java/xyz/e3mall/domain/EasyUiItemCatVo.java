package xyz.e3mall.domain;

import java.io.Serializable;

public class EasyUiItemCatVo implements Serializable {

    long id;
    String text;
    String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EasyUiItemCatVo{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
