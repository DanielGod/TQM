package tqm.bianfeng.com.tqm.lawhelp.tools;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.CharacterParser;
import tqm.bianfeng.com.tqm.pojo.cityInfo;

/**
 * Created by johe on 2017/3/31.
 */

public class ThreeAddTools {

    List<cityInfo> cityInfoList;
    CharacterParser characterParser;
    public static class Helper {
        public static ThreeAddTools threeAddTools = new ThreeAddTools();
    }

    public static ThreeAddTools getTools() {
        return Helper.threeAddTools;
    }

    public List<cityInfo> readAllCity(Context context) {
        if (cityInfoList == null) {
            cityInfoList=new ArrayList<>();
            characterParser=CharacterParser.getInstance();
            String addJson = context.getResources().getString(R.string.city_json);
            try {
                JSONArray datas = new JSONArray(addJson);
                int length = datas.length();
                for (int i = 0; i < length; i++) {
                    JSONObject dataObj = datas.getJSONObject(i);
                    //省
                    String province = dataObj.getString("name");

                    JSONArray citysJson = dataObj.getJSONArray("city");
                    int citysJsonlength = citysJson.length();
                    for (int c = 0; c < citysJsonlength; c++) {
                        JSONObject cityObj = citysJson.getJSONObject(c);
                        //市
                        cityInfo cityInfo = new cityInfo();
                        cityInfo.setProvince(province);
                        cityInfo.setCity(cityObj.getString("name"));

                        //汉字转换成拼音
                        String pinyin = characterParser.getSelling(cityObj.getString("name"));
                        String sortString = pinyin.substring(0, 1).toUpperCase();

                        // 正则表达式，判断首字母是否是英文字母
                        if(sortString.matches("[A-Z]")){
                            cityInfo.setSortKey(sortString.toUpperCase());
                        }else{
                            cityInfo.setSortKey("#");
                        }

                        cityInfoList.add(cityInfo);


                        JSONArray areaJson = cityObj.getJSONArray("area");
                        int areaLength = areaJson.length();
                        ArrayList<String> area = new ArrayList<>();
                        for (int a = 0; a < areaLength; a++) {
                            area.add(areaJson.getString(a));
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.i("gqf", "cityInfoList" + cityInfoList);
        return cityInfoList;
    }

    public List<String> getDistrictsByCity(Context context, String city) {
        List<String> Districts = new ArrayList<>();
        String addJson = context.getResources().getString(R.string.city_json);
        try {
            JSONArray datas = new JSONArray(addJson);
            int length = datas.length();
            for (int i = 0; i < length; i++) {
                JSONObject dataObj = datas.getJSONObject(i);
                //省
                //Log.i("gqf", length + "dataObj" + dataObj.getString("name"));
                JSONArray citysJson = dataObj.getJSONArray("city");
                int citysJsonlength = citysJson.length();
                for (int c = 0; c < citysJsonlength; c++) {
                    JSONObject cityObj = citysJson.getJSONObject(c);
                    //市
                    //Log.i("gqf", citysJsonlength + "cityObj" + cityObj.getString("name"));

                    if (cityObj.getString("name").equals(city)) {
                        JSONArray areaJson = cityObj.getJSONArray("area");
                        int areaLength = areaJson.length();
                        for (int a = 0; a < areaLength; a++) {
                            Log.i("gqf", areaLength + "areaJson" +areaJson.getString(a));
                            Districts.add(areaJson.getString(a));
                        }
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Districts;
    }
}
