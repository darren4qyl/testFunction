package b.com.myapplication.mode;

import java.io.Serializable;
import java.util.Date;

public class DeviceInfoData implements Serializable {
    private Long id;
    private String name;
    private java.util.Date date;

    public boolean isVisibility() {
        return visibility;
    }

    public DeviceInfoData setVisibility(boolean visibility) {
        this.visibility = visibility;
        return this;
    }

    private boolean visibility = true;

    public DeviceInfoData(DeviceInfo info){
        this.id = info.getId();
        this.name = info.getName();
        this.date = info.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
