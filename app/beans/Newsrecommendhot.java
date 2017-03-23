package beans;

import java.util.Date;

/**
 * Created by zhangshl on 16/8/22.
 */
public class Newsrecommendhot {
    private int nid;
    private Date ctime;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
