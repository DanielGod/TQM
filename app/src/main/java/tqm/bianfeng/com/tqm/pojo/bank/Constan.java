package tqm.bianfeng.com.tqm.pojo.bank;

import android.util.Log;

import java.util.List;

import tqm.bianfeng.com.tqm.main.vehicle.bean.Contact;

/**
 * Created by Daniel on 2017/3/14.
 */

public class Constan {
    public static String LOGTAGNAME = "Daniel";
    public static final String DESC = "desc";//降序
    public static String HOMESHOW_TRUE = "01";//首页展示
    public static String HOMESHOW_FALSE = "02";//非首页
    public static int FIRSTPAGESIZE = 2;//首页展示条数
    public static int PAGESIZE = 10;//非首页展示条数
    public static int FIRSTPAGENUM = 0;//第一页
    public static boolean PULLUP = true; //上拉
    public static boolean NOTPULLUP = false; //非上拉
    public static String SEARCHFILTER = "中国银行"; //poi 搜索条件pageCapacity
    public static int SEARCHRADIUS= 5000; //poi 搜索半径
    public static int PAGECAPACITY= 10; //poi 搜索条目
    public static int GRIDLAYOUTSIZE= 3; //gridlayout 列数
    public static String LOCATION = "上海";
    public static String IMGURL = "?imageView/s/300x230";
    public static String localVersion = "";

    public static String JHKEY = "31aeb5da41235191329d5d3c80e67b1c";//聚合数据密钥key
    public static int REQUEST_cardcollaction_IMAGE = 1000;
    //加载
    public static int LOADING = 1;//加载中
    public static int LOAD_SUCCESS = 2;//加载成功
    public static int LOAD_NULL = 3;//加载数据空
    public static int LOAD_FAILED = 4;//加载失败
    //权限
    public final static int REQUEST_CODE_PERMISSION_CONTACTS = 14;//通讯录
    public final static int REQUEST_CODE_CAMERA = 13;//相机
    public final static int REQUEST_CODE_EXTERNAL_STORAGE = 12;//储存
    public final static int REQUEST_CODE_READ_PHONE_STATE = 11;//获取手机设备权限
    public final static int REQUEST_CODE_ALL_PERMISSIONS = 10;//获取所需的所有权限
    public final static int REQUEST_CODE_LOCATIONI = 9;//获取位置权限

    //共有集合
    public static List<Contact> mContacts;//联系人列表

    //渠道
    public static String CHANNEL = "UMENG_CHANNEL";//友盟渠道名


    public static void log(String msg){
        Log.e(LOGTAGNAME,msg);
    }


}
