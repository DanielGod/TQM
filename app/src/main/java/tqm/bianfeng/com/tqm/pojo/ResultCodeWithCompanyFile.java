package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/4/12.
 */

public class ResultCodeWithCompanyFile extends ResultCode{

    String companyFile;

    public String getCompanyFile() {
        return companyFile;
    }

    public void setCompanyFile(String companyFile) {
        this.companyFile = companyFile;
    }

    @Override
    public String toString() {
        return "ResultCodeWithCompanyFile{" +
                "companyFile='" + companyFile + '\'' +
                '}';
    }
}
