package b.com.myapplication.mode;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import java.util.Date;

@Entity(indexes = {
        @Index(value = "id, name, date DESC", unique = true)
})
public class DeviceInfo {
    private Long id;
    @NotNull
    private String name;

    private java.util.Date date;

    @Generated(hash = 1534115748)
    public DeviceInfo(Long id, @NotNull String name, java.util.Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
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

    public java.util.Date getDate() {
        return this.date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }
}
