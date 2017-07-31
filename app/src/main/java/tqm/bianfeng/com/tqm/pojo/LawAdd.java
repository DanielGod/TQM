package tqm.bianfeng.com.tqm.pojo;

import com.google.gson.Gson;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by johe on 2017/3/30.
 */

public class LawAdd extends RealmObject{
    @PrimaryKey
    private int id;
    private String province="";
    private String city="";
    private String district="";
    private String specialField="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSpecialField() {
        return specialField;
    }

    public void setSpecialField(String specialField) {
        this.specialField = specialField;
    }

    public String getQueryParams(){
        Gson gson=new Gson();
        return gson.toJson(new queryParams(province,city,district,specialField));
    }

    @Override
    public String toString() {
        return "LawAdd{" +
                "id=" + id +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", specialField='" + specialField + '\'' +
                '}';
    }
    class queryParams{
        String province="";
        String city="";
        String district="";
        String specialField="";
        queryParams(String p,
                String c,
                String d,
                String s){
            province=p;
            city=c;
            district=d;
            specialField=s;
        }
    }
}
