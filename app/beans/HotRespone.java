package beans;

import java.util.List;

/**
 * Created by zhangshl on 16/9/5.
 */
public class HotRespone {
    private Long update_time;
    private List<HotNews> result;
    private int ret_code;

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public List<HotNews> getResult() {
        return result;
    }

    public void setResult(List<HotNews> result) {
        this.result = result;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}
