package beans;

import java.util.List;

/**
 * Created by zhangshl on 16/9/5.
 */
public class ChidRespone {
    private boolean bSuccess;
    private List<NidChid> result;

    public boolean isbSuccess() {
        return bSuccess;
    }

    public void setbSuccess(boolean bSuccess) {
        this.bSuccess = bSuccess;
    }

    public List<NidChid> getResult() {
        return result;
    }

    public void setResult(List<NidChid> result) {
        this.result = result;
    }
}
