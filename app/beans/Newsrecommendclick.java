package beans;

import java.util.Date;

/**
 * Created by zhangshl on 16/8/22.
 */
public class Newsrecommendclick {
    private int uid;
    private int nid;
    private int chid;
    private Date ctime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public int getChid() {
        return chid;
    }

    public void setChid(int chid) {
        this.chid = chid;
    }
}
