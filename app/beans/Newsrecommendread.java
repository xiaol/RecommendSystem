package beans;

import java.sql.Timestamp;

/**
 * Created by zhangshl on 16/8/22.
 */
public class Newsrecommendread {
    private int uid;
    private int nid;
    private int chid;
    private Timestamp readtime;

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

    public Timestamp getReadtime() {
        return readtime;
    }

    public void setReadtime(Timestamp readtime) {
        this.readtime = readtime;
    }

    public int getChid() {
        return chid;
    }

    public void setChid(int chid) {
        this.chid = chid;
    }
}
