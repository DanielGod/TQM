package tqm.bianfeng.com.tqm.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by johe on 2017/3/14.
 */

public class MyAttention {
    /**
     * 01 : 1
     * 02 : 1
     * 03 : 1
     */

    @SerializedName("01")
    private int _$01;
    @SerializedName("02")
    private int _$02;
    @SerializedName("03")
    private int _$03;

    public int get_$01() {
        return _$01;
    }

    public void set_$01(int _$01) {
        this._$01 = _$01;
    }

    public int get_$02() {
        return _$02;
    }

    public void set_$02(int _$02) {
        this._$02 = _$02;
    }

    public int get_$03() {
        return _$03;
    }

    public void set_$03(int _$03) {
        this._$03 = _$03;
    }

    @Override
    public String toString() {
        return "MyAttention{" +
                "_$01=" + _$01 +
                ", _$02=" + _$02 +
                ", _$03=" + _$03 +
                '}';
    }
}
