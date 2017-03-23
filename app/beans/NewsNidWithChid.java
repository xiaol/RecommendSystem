package beans;

import java.util.Date;

/**
 * Created by zhangshl on 16/8/22.
 */
public class NewsNidWithChid {
    private int nid;
    private int chid;
    private Date ctime;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getChid() {
        return chid;
    }

    public void setChid(int chid) {
        this.chid = chid;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}
