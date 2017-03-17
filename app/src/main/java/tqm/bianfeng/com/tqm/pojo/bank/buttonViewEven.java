package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/16.
 */

public class buttonViewEven {
    String buttonView;
    boolean flag;

    public buttonViewEven(String buttonView, boolean flag) {
        this.buttonView = buttonView;
        this.flag = flag;
    }

    public String getButtonView() {
        return buttonView;
    }

    public void setButtonView(String buttonView) {
        this.buttonView = buttonView;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
