package tqm.bianfeng.com.tqm.pojo.result;

import java.util.List;

import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/4/12.
 */

public class ResultCodeWithImgPathList extends ResultCode{

    List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
