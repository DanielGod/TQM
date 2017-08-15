package tqm.bianfeng.com.tqm.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import tqm.bianfeng.com.tqm.BuildConfig;
import tqm.bianfeng.com.tqm.Dialog.BaseDialog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

import static com.blankj.utilcode.utils.ConstUtils.BYTE;
import static com.blankj.utilcode.utils.ConstUtils.DAY;
import static com.blankj.utilcode.utils.ConstUtils.GB;
import static com.blankj.utilcode.utils.ConstUtils.HOUR;
import static com.blankj.utilcode.utils.ConstUtils.KB;
import static com.blankj.utilcode.utils.ConstUtils.MB;
import static com.blankj.utilcode.utils.ConstUtils.MIN;
import static com.blankj.utilcode.utils.ConstUtils.MSEC;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_CHZ;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_DATE;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_EMAIL;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_IDCARD15;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_IDCARD18;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_IP;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_MOBILE_EXACT;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_MOBILE_SIMPLE;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_TEL;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_URL;
import static com.blankj.utilcode.utils.ConstUtils.REGEX_USERNAME;
import static com.blankj.utilcode.utils.ConstUtils.SEC;
import static com.blankj.utilcode.utils.ConvertUtils.bytes2HexString;
import static com.blankj.utilcode.utils.ConvertUtils.hexString2Bytes;

/**
 * 通用工具类
 * Created by 王九东 on 2017/8/4.
 */

public class GeneralTools {

    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : App相关工具类
     * </pre>
     */
    public static class AppUtils {

        private AppUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 获取安装App(支持6.0)的意图
         *
         * @param filePath 文件路径
         * @return 意图
         */
        public static Intent getInstallAppIntent(String filePath) {
            return getInstallAppIntent(FileUtils.getFileByPath(filePath));
        }

        /**
         * 获取安装App(支持6.0)的意图
         *
         * @param file 文件
         * @return 意图
         */
        public static Intent getInstallAppIntent(File file) {
            if (file == null) return null;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String type;
            if (Build.VERSION.SDK_INT < 23) {
                type = "application/vnd.android.package-archive";
            } else {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
            }
            return intent.setDataAndType(Uri.fromFile(file), type);
        }

        /**
         * 获取卸载App的意图
         *
         * @param packageName 包名
         * @return 意图
         */
        public Intent getUninstallAppIntent(String packageName) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        /**
         * 获取打开App的意图
         *
         * @param context     上下文
         * @param packageName 包名
         * @return 意图
         */
        public static Intent getOpenAppItent(Context context, String packageName) {
            return getIntentByPackageName(context, packageName);
        }

        /**
         * 获取App信息的意图
         *
         * @param packageName 包名
         * @return 意图
         */
        public static Intent getAppInfoIntent(String packageName) {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            return intent.setData(Uri.parse("package:" + packageName));
        }

        /**
         * 获取App信息分享的意图
         *
         * @param info 分享信息
         * @return 意图
         */
        public static Intent getShareInfoIntent(String info) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            return intent.putExtra(Intent.EXTRA_TEXT, info);
        }

        /**
         * 判断App是否安装
         *
         * @param context     上下文
         * @param packageName 包名
         * @return {@code true}: 已安装<br>{@code false}: 未安装
         */
        public static boolean isInstallApp(Context context, String packageName) {
            return getIntentByPackageName(context, packageName) != null;
        }

        /**
         * 根据包名获取意图
         *
         * @param context     上下文
         * @param packageName 包名
         * @return Intent
         */
        private static Intent getIntentByPackageName(Context context, String packageName) {
            return context.getPackageManager().getLaunchIntentForPackage(packageName);
        }

        /**
         * 封装App信息的Bean类
         */
        public static class AppInfo {

            private String name;
            private Drawable icon;
            private String packageName;
            private String packagePath;
            private String versionName;
            private int versionCode;
            private boolean isSD;
            private boolean isUser;

            public Drawable getIcon() {
                return icon;
            }

            public void setIcon(Drawable icon) {
                this.icon = icon;
            }

            public boolean isSD() {
                return isSD;
            }

            public void setSD(boolean SD) {
                isSD = SD;
            }

            public boolean isUser() {
                return isUser;
            }

            public void setUser(boolean user) {
                isUser = user;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packagName) {
                this.packageName = packagName;
            }

            public String getPackagePath() {
                return packagePath;
            }

            public void setPackagePath(String packagePath) {
                this.packagePath = packagePath;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            /**
             * @param name        名称
             * @param icon        图标
             * @param packageName 包名
             * @param packagePath 包路径
             * @param versionName 版本号
             * @param versionCode 版本Code
             * @param isSD        是否安装在SD卡
             * @param isUser      是否是用户程序
             */
            public AppInfo(String name, Drawable icon, String packageName, String packagePath,
                           String versionName, int versionCode, boolean isSD, boolean isUser) {
                this.setName(name);
                this.setIcon(icon);
                this.setPackageName(packageName);
                this.setPackagePath(packagePath);
                this.setVersionName(versionName);
                this.setVersionCode(versionCode);
                this.setSD(isSD);
                this.setUser(isUser);
            }

            //        @Override
            //        public String toString() {
            //            return getName() + "\n"
            //                    + getIcon() + "\n"
            //                    + getPackageName() + "\n"
            //                    + getPackagePath() + "\n"
            //                    + getVersionName() + "\n"
            //                    + getVersionCode() + "\n"
            //                    + isSD() + "\n"
            //                    + isUser() + "\n";
            //        }
        }

        /**
         * 获取当前App信息
         * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
         *
         * @param context 上下文
         * @return 当前应用的AppInfo
         */
        public static com.blankj.utilcode.utils.AppUtils.AppInfo getAppInfo(Context context) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = null;
            try {
                pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return pi != null ? getBean(pm, pi) : null;
        }

        /**
         * 得到AppInfo的Bean
         *
         * @param pm 包的管理
         * @param pi 包的信息
         * @return AppInfo类
         */
        private static com.blankj.utilcode.utils.AppUtils.AppInfo getBean(PackageManager pm, PackageInfo pi) {
            ApplicationInfo ai = pi.applicationInfo;
            String name = ai.loadLabel(pm).toString();
            Drawable icon = ai.loadIcon(pm);
            String packageName = pi.packageName;
            String packagePath = ai.sourceDir;
            String versionName = pi.versionName;
            int versionCode = pi.versionCode;
            boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
            boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
            return new com.blankj.utilcode.utils.AppUtils.AppInfo(name, icon, packageName, packagePath, versionName, versionCode, isSD, isUser);
        }

        /**
         * 获取所有已安装App信息
         * <p>{@link #getBean(PackageManager, PackageInfo)}（名称，图标，包名，包路径，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
         * <p>依赖上面的getBean方法</p>
         *
         * @param context 上下文
         * @return 所有已安装的AppInfo列表
         */
        public static List<com.blankj.utilcode.utils.AppUtils.AppInfo> getAllAppsInfo(Context context) {
            List<com.blankj.utilcode.utils.AppUtils.AppInfo> list = new ArrayList<>();
            PackageManager pm = context.getPackageManager();
            // 获取系统中安装的所有软件信息
            List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
            for (PackageInfo pi : installedPackages) {
                if (pi != null) {
                    list.add(getBean(pm, pi));
                }
            }
            return list;
        }

        /**
         * 判断当前App处于前台还是后台
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
         * <p>并且必须是系统应用该方法才有效</p>
         *
         * @param context 上下文
         * @return {@code true}: 后台<br>{@code false}: 前台
         */
        public static boolean isAppBackground(Context context) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            @SuppressWarnings("deprecation")
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (!topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/11
     *     desc  : 常量相关工具类
     * </pre>
     */
    public static class ConstUtils {

        private ConstUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /******************** 存储相关常量 ********************/
        /**
         * Byte与Byte的倍数
         */
        public static final int BYTE = 1;
        /**
         * KB与Byte的倍数
         */
        public static final int KB = 1024;
        /**
         * MB与Byte的倍数
         */
        public static final int MB = 1048576;
        /**
         * GB与Byte的倍数
         */
        public static final int GB = 1073741824;

        public enum MemoryUnit {
            BYTE,
            KB,
            MB,
            GB
        }

        /******************** 时间相关常量 ********************/
        /**
         * 毫秒与毫秒的倍数
         */
        public static final int MSEC = 1;
        /**
         * 秒与毫秒的倍数
         */
        public static final int SEC = 1000;
        /**
         * 分与毫秒的倍数
         */
        public static final int MIN = 60000;
        /**
         * 时与毫秒的倍数
         */
        public static final int HOUR = 3600000;
        /**
         * 天与毫秒的倍数
         */
        public static final int DAY = 86400000;

        public enum TimeUnit {
            MSEC,
            SEC,
            MIN,
            HOUR,
            DAY
        }

        /******************** 正则相关常量 ********************/
        /**
         * 正则：手机号（简单）
         */
        public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
        /**
         * 正则：手机号（精确）
         * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
         * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
         * <p>电信：133、153、173、177、180、181、189</p>
         * <p>全球星：1349</p>
         * <p>虚拟运营商：170</p>
         */
        public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
        /**
         * 正则：电话号码
         */
        public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
        /**
         * 正则：身份证号码15位
         */
        public static final String REGEX_IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        /**
         * 正则：身份证号码18位
         */
        public static final String REGEX_IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        /**
         * 正则：邮箱
         */
        public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        /**
         * 正则：URL
         */
        public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
        /**
         * 正则：汉字
         */
        public static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
        /**
         * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
         */
        public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
        /**
         * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
         */
        public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
        /**
         * 正则：IP地址
         */
        public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    }

    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/13
     *     desc  : 转换相关工具类
     * </pre>
     */
    public static class ConvertUtils {

        static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        /**
         * byteArr转hexString
         * <p>例如：</p>
         * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
         *
         * @param bytes byte数组
         * @return 16进制大写字符串
         */
        public static String bytes2HexString(byte[] bytes) {
            char[] ret = new char[bytes.length << 1];
            for (int i = 0, j = 0; i < bytes.length; i++) {
                ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
                ret[j++] = hexDigits[bytes[i] & 0x0f];
            }
            return new String(ret);
        }

        /**
         * hexString转byteArr
         * <p>例如：</p>
         * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
         *
         * @param hexString 十六进制字符串
         * @return 字节数组
         */
        public static byte[] hexString2Bytes(String hexString) {
            int len = hexString.length();
            if (len % 2 != 0) {
                throw new IllegalArgumentException("长度不是偶数");
            }
            char[] hexBytes = hexString.toUpperCase().toCharArray();
            byte[] ret = new byte[len >>> 1];
            for (int i = 0; i < len; i += 2) {
                ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
            }
            return ret;
        }

        /**
         * hexChar转int
         *
         * @param hexChar hex单个字节
         * @return 0..15
         */
        private static int hex2Dec(char hexChar) {
            if (hexChar >= '0' && hexChar <= '9') {
                return hexChar - '0';
            } else if (hexChar >= 'A' && hexChar <= 'F') {
                return hexChar - 'A' + 10;
            } else {
                throw new IllegalArgumentException();
            }
        }

        /**
         * charArr转byteArr
         *
         * @param chars 字符数组
         * @return 字节数组
         */
        public static byte[] chars2Bytes(char[] chars) {
            int len = chars.length;
            byte[] bytes = new byte[len];
            for (int i = 0; i < len; i++) {
                bytes[i] = (byte) (chars[i]);
            }
            return bytes;
        }

        /**
         * byteArr转charArr
         *
         * @param bytes 字节数组
         * @return 字符数组
         */
        public static char[] bytes2Chars(byte[] bytes) {
            int len = bytes.length;
            char[] chars = new char[len];
            for (int i = 0; i < len; i++) {
                chars[i] = (char) (bytes[i] & 0xff);
            }
            return chars;
        }

        /**
         * 字节数转以unit为单位的size
         *
         * @param byteNum 字节数
         * @param unit    <ul>
         *                <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#BYTE}: 字节</li>
         *                <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#KB}  : 千字节</li>
         *                <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#MB}  : 兆</li>
         *                <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#GB}  : GB</li>
         *                </ul>
         * @return 以unit为单位的size
         */
        public static double byte2Size(long byteNum, com.blankj.utilcode.utils.ConstUtils.MemoryUnit unit) {
            if (byteNum < 0) return -1;
            switch (unit) {
                default:
                case BYTE:
                    return (double) byteNum / BYTE;
                case KB:
                    return (double) byteNum / KB;
                case MB:
                    return (double) byteNum / MB;
                case GB:
                    return (double) byteNum / GB;
            }
        }

        /**
         * 以unit为单位的size转字节数
         *
         * @param size 大小
         * @param unit <ul>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#BYTE}: 字节</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#KB}  : 千字节</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#MB}  : 兆</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#GB}  : GB</li>
         *             </ul>
         * @return 字节数
         */
        public static long size2Byte(long size, com.blankj.utilcode.utils.ConstUtils.MemoryUnit unit) {
            if (size < 0) return -1;
            switch (unit) {
                default:
                case BYTE:
                    return size * BYTE;
                case KB:
                    return size * KB;
                case MB:
                    return size * MB;
                case GB:
                    return size * GB;
            }
        }

        /**
         * 字节数转合适大小
         * <p>保留3位小数</p>
         *
         * @param byteNum 字节数
         * @return 1...1024 unit
         */
        public static String byte2FitSize(long byteNum) {
            if (byteNum < 0) {
                return "shouldn't be less than zero!";
            } else if (byteNum < KB) {
                return String.format(Locale.getDefault(), "%.3fB", (double) byteNum);
            } else if (byteNum < MB) {
                return String.format(Locale.getDefault(), "%.3fKB", (double) byteNum / KB);
            } else if (byteNum < GB) {
                return String.format(Locale.getDefault(), "%.3fMB", (double) byteNum / MB);
            } else {
                return String.format(Locale.getDefault(), "%.3fGB", (double) byteNum / GB);
            }
        }

        /**
         * inputStream转outputStream
         *
         * @param is 输入流
         * @return outputStream子类
         */
        public static ByteArrayOutputStream input2OutputStream(InputStream is) {
            if (is == null) return null;
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] b = new byte[KB];
                int len;
                while ((len = is.read(b, 0, KB)) != -1) {
                    os.write(b, 0, len);
                }
                return os;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                FileUtils.closeIO(is);
            }
        }

        /**
         * outputStream转inputStream
         *
         * @param out 输出流
         * @return inputStream子类
         */
        public ByteArrayInputStream output2InputStream(OutputStream out) {
            if (out == null) return null;
            return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        }

        /**
         * inputStream转byteArr
         *
         * @param is 输入流
         * @return 字节数组
         */
        public static byte[] inputStream2Bytes(InputStream is) {
            return input2OutputStream(is).toByteArray();
        }

        /**
         * byteArr转inputStream
         *
         * @param bytes 字节数组
         * @return 输入流
         */
        public static InputStream bytes2InputStream(byte[] bytes) {
            return new ByteArrayInputStream(bytes);
        }

        /**
         * outputStream转byteArr
         *
         * @param out 输出流
         * @return 字节数组
         */
        public static byte[] outputStream2Bytes(OutputStream out) {
            if (out == null) return null;
            return ((ByteArrayOutputStream) out).toByteArray();
        }

        /**
         * outputStream转byteArr
         *
         * @param bytes 字节数组
         * @return 字节数组
         */
        public static OutputStream bytes2OutputStream(byte[] bytes) {
            ByteArrayOutputStream os = null;
            try {
                os = new ByteArrayOutputStream();
                os.write(bytes);
                return os;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                FileUtils.closeIO(os);
            }
        }

        /**
         * inputStream转string按编码
         *
         * @param is          输入流
         * @param charsetName 编码格式
         * @return 字符串
         */
        public static String inputStream2String(InputStream is, String charsetName) {
            if (is == null || StringUtils.isSpace(charsetName)) return null;
            try {
                return new String(inputStream2Bytes(is), charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * string转inputStream按编码
         *
         * @param string      字符串
         * @param charsetName 编码格式
         * @return 输入流
         */
        public static InputStream string2InputStream(String string, String charsetName) {
            if (string == null || StringUtils.isSpace(charsetName)) return null;
            try {
                return new ByteArrayInputStream(string.getBytes(charsetName));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * outputStream转string按编码
         *
         * @param out         输出流
         * @param charsetName 编码格式
         * @return 字符串
         */
        public static String outputStream2String(OutputStream out, String charsetName) {
            if (out == null) return null;
            try {
                return new String(outputStream2Bytes(out), charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * string转outputStream按编码
         *
         * @param string      字符串
         * @param charsetName 编码格式
         * @return 输入流
         */
        public static OutputStream string2OutputStream(String string, String charsetName) {
            if (string == null || StringUtils.isSpace(charsetName)) return null;
            try {
                return bytes2OutputStream(string.getBytes(charsetName));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * bitmap转byteArr
         *
         * @param bitmap bitmap对象
         * @param format 格式
         * @return 字节数组
         */
        public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
            if (bitmap == null) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(format, 100, baos);
            return baos.toByteArray();
        }

        /**
         * byteArr转bitmap
         *
         * @param bytes 字节数组
         * @return bitmap对象
         */
        public static Bitmap bytes2Bitmap(byte[] bytes) {
            return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        /**
         * drawable转bitmap
         *
         * @param drawable drawable对象
         * @return bitmap对象
         */
        public static Bitmap drawable2Bitmap(Drawable drawable) {
            return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
        }

        /**
         * bitmap转drawable
         *
         * @param res    resources对象
         * @param bitmap bitmap对象
         * @return drawable对象
         */
        public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
            return bitmap == null ? null : new BitmapDrawable(res, bitmap);
        }

        /**
         * drawable转byteArr
         *
         * @param drawable drawable对象
         * @param format   格式
         * @return 字节数组
         */
        public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
            return bitmap2Bytes(drawable2Bitmap(drawable), format);
        }

        /**
         * byteArr转drawable
         *
         * @param res   resources对象
         * @param bytes 字节数组
         * @return drawable对象
         */
        public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
            return bitmap2Drawable(res, bytes2Bitmap(bytes));
        }

        /**
         * dp转px
         *
         * @param context 上下文
         * @param dpValue dp值
         * @return px值
         */
        public static int dp2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        /**
         * px转dp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return dp值
         */
        public static int px2dp(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        /**
         * sp转px
         *
         * @param context 上下文
         * @param spValue sp值
         * @return px值
         */
        public static int sp2px(Context context, float spValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        }

        /**
         * px转sp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return sp值
         */
        public static int px2sp(Context context, float pxValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (pxValue / fontScale + 0.5f);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/12
     *     desc  : 图片相关工具类
     * </pre>
     */
    public static class ImageUtils {

        private ImageUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * bitmap 转 file
         * @param bitmap
         * @param fileName
         */

        public static File saveBitmapFile(Bitmap bitmap,String fileName) {
            Constan.log("图片名称："+ fileName);
            Constan.log("图片bitmap："+ bitmap);
            File mZipImg = null;
            try {
                if (FileUtils.createOrExistsDir("/storage/emulated/0/zipBitMap")){//保存图片的文件夹
                    mZipImg = new File("/storage/emulated/0/zipBitMap/"+fileName);//将要保存图片的路径
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mZipImg));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);//根据图片质量压缩 1-100
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mZipImg;
        }

        /**
         * 回收bitmap
         * @param bitmap
         */
        public static void bitMapRecycled(Bitmap bitmap){
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                bitmap= null;
            }
        }

        /**
         * bitmap转byteArr
         *
         * @param bitmap bitmap对象
         * @param format 格式
         * @return 字节数组
         */
        public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
            return com.blankj.utilcode.utils.ConvertUtils.bitmap2Bytes(bitmap, format);
        }

        /**
         * byteArr转bitmap
         *
         * @param bytes 字节数组
         * @return bitmap对象
         */
        public static Bitmap bytes2Bitmap(byte[] bytes) {
            return com.blankj.utilcode.utils.ConvertUtils.bytes2Bitmap(bytes);
        }

        /**
         * drawable转bitmap
         *
         * @param drawable drawable对象
         * @return bitmap对象
         */
        public static Bitmap drawable2Bitmap(Drawable drawable) {
            return com.blankj.utilcode.utils.ConvertUtils.drawable2Bitmap(drawable);
        }

        /**
         * bitmap转drawable
         *
         * @param res    resources对象
         * @param bitmap bitmap对象
         * @return drawable对象
         */
        public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
            return com.blankj.utilcode.utils.ConvertUtils.bitmap2Drawable(res, bitmap);
        }

        /**
         * drawable转byteArr
         *
         * @param drawable drawable对象
         * @param format   格式
         * @return 字节数组
         */
        public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
            return com.blankj.utilcode.utils.ConvertUtils.drawable2Bytes(drawable, format);
        }

        /**
         * byteArr转drawable
         *
         * @param res   resources对象
         * @param bytes 字节数组
         * @return drawable对象
         */
        public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
            return com.blankj.utilcode.utils.ConvertUtils.bytes2Drawable(res, bytes);
        }

        /**
         * 计算采样大小
         *
         * @param options   选项
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return 采样大小
         */
        private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
            if (maxWidth == 0 || maxHeight == 0) return 1;
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;
            while ((height >>= 1) >= maxHeight && (width >>= 1) >= maxWidth) {
                inSampleSize <<= 1;
            }
            return inSampleSize;
        }

        /**
         * 获取bitmap
         *
         * @param file 文件
         * @return bitmap
         */
        public static Bitmap getBitmap(File file) {
            if (file == null) return null;
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                FileUtils.closeIO(is);
            }
        }

        /**
         * 获取bitmap
         *
         * @param file      文件
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(File file, int maxWidth, int maxHeight) {
            if (file == null) return null;
            InputStream is = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                is = new BufferedInputStream(new FileInputStream(file));
                BitmapFactory.decodeStream(is, null, options);
                options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(is, null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                FileUtils.closeIO(is);
            }
        }

        /**
         * 获取bitmap
         *
         * @param filePath 文件路径
         * @return bitmap
         */
        public static Bitmap getBitmap(String filePath) {
            if (StringUtils.isSpace(filePath)) return null;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
            return BitmapFactory.decodeFile(filePath);
        }

        /**
         * 获取bitmap
         *
         * @param filePath  文件路径
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(String filePath, int maxWidth, int maxHeight) {
            if (StringUtils.isSpace(filePath)) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }

        /**
         * 获取bitmap
         *
         * @param is 输入流
         * @return bitmap
         */
        public Bitmap getBitmap(InputStream is) {
            if (is == null) return null;
            return BitmapFactory.decodeStream(is);
        }

        /**
         * 获取bitmap
         *
         * @param is        输入流
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(InputStream is, int maxWidth, int maxHeight) {
            if (is == null) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(is, null, options);
        }

        /**
         * 获取bitmap
         *
         * @param data   数据
         * @param offset 偏移量
         * @return bitmap
         */
        public Bitmap getBitmap(byte[] data, int offset) {
            if (data.length == 0) return null;
            return BitmapFactory.decodeByteArray(data, offset, data.length);
        }

        /**
         * 获取bitmap
         *
         * @param data      数据
         * @param offset    偏移量
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(byte[] data, int offset, int maxWidth, int maxHeight) {
            if (data.length == 0) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, offset, data.length, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(data, offset, data.length, options);
        }

        /**
         * 获取bitmap
         *
         * @param context 上下文
         * @param resId   资源id
         * @return bitmap
         */
        public static Bitmap getBitmap(Context context, int resId) {
            if (context == null) return null;
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is);
        }

        /**
         * 获取bitmap
         *
         * @param context   上下文
         * @param resId     资源id
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(Context context, int resId, int maxWidth, int maxHeight) {
            if (context == null) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream is = context.getResources().openRawResource(resId);
            BitmapFactory.decodeStream(is, null, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(is, null, options);
        }

        /**
         * 获取bitmap
         *
         * @param res 资源对象
         * @param id  资源id
         * @return bitmap
         */
        public static Bitmap getBitmap(Resources res, int id) {
            if (res == null) return null;
            return BitmapFactory.decodeResource(res, id);
        }

        /**
         * 获取bitmap
         *
         * @param res       资源对象
         * @param id        资源id
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(Resources res, int id, int maxWidth, int maxHeight) {
            if (res == null) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, id, options);
        }

        /**
         * 获取bitmap
         *
         * @param fd 文件描述
         * @return bitmap
         */
        public static Bitmap getBitmap(FileDescriptor fd) {
            if (fd == null) return null;
            return BitmapFactory.decodeFileDescriptor(fd);
        }

        /**
         * 获取bitmap
         *
         * @param fd        文件描述
         * @param maxWidth  最大宽度
         * @param maxHeight 最大高度
         * @return bitmap
         */
        public static Bitmap getBitmap(FileDescriptor fd, int maxWidth, int maxHeight) {
            if (fd == null) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        }

        /**
         * 缩放图片
         *
         * @param src       源图片
         * @param newWidth  新宽度
         * @param newHeight 新高度
         * @return 缩放后的图片
         */
        public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
            return scale(src, newWidth, newHeight, false);
        }

        /**
         * 缩放图片
         *
         * @param src       源图片
         * @param newWidth  新宽度
         * @param newHeight 新高度
         * @return 缩放后的图片
         */
        public static Bitmap scale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 缩放图片
         *
         * @param src         源图片
         * @param scaleWidth  缩放宽度倍数
         * @param scaleHeight 缩放高度倍数
         * @return 缩放后的图片
         */
        public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight) {
            return scale(src, scaleWidth, scaleHeight, false);
        }

        /**
         * 缩放图片
         *
         * @param src         源图片
         * @param scaleWidth  缩放宽度倍数
         * @param scaleHeight 缩放高度倍数
         * @param recycle     是否回收
         * @return 缩放后的图片
         */
        public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Matrix matrix = new Matrix();
            matrix.setScale(scaleWidth, scaleHeight);
            Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 裁剪图片
         *
         * @param src    源图片
         * @param x      开始坐标x
         * @param y      开始坐标y
         * @param width  裁剪宽度
         * @param height 裁剪高度
         * @return 裁剪后的图片
         */
        public static Bitmap clip(Bitmap src, int x, int y, int width, int height) {
            return clip(src, x, y, width, height, false);
        }

        /**
         * 裁剪图片
         *
         * @param src     源图片
         * @param x       开始坐标x
         * @param y       开始坐标y
         * @param width   裁剪宽度
         * @param height  裁剪高度
         * @param recycle 是否回收
         * @return 裁剪后的图片
         */
        public static Bitmap clip(Bitmap src, int x, int y, int width, int height, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Bitmap ret = Bitmap.createBitmap(src, x, y, width, height);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 倾斜图片
         *
         * @param src 源图片
         * @param kx  倾斜因子x
         * @param ky  倾斜因子y
         * @return 倾斜后的图片
         */
        public static Bitmap skew(Bitmap src, float kx, float ky) {
            return skew(src, kx, ky, 0, 0, false);
        }

        /**
         * 倾斜图片
         *
         * @param src     源图片
         * @param kx      倾斜因子x
         * @param ky      倾斜因子y
         * @param recycle 是否回收
         * @return 倾斜后的图片
         */
        public static Bitmap skew(Bitmap src, float kx, float ky, boolean recycle) {
            return skew(src, kx, ky, 0, 0, recycle);
        }

        /**
         * 倾斜图片
         *
         * @param src 源图片
         * @param kx  倾斜因子x
         * @param ky  倾斜因子y
         * @param px  平移因子x
         * @param py  平移因子y
         * @return 倾斜后的图片
         */
        public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py) {
            return skew(src, kx, ky, 0, 0, false);
        }

        /**
         * 倾斜图片
         *
         * @param src     源图片
         * @param kx      倾斜因子x
         * @param ky      倾斜因子y
         * @param px      平移因子x
         * @param py      平移因子y
         * @param recycle 是否回收
         * @return 倾斜后的图片
         */
        public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Matrix matrix = new Matrix();
            matrix.setSkew(kx, ky, px, py);
            Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 旋转图片
         *
         * @param src     源图片
         * @param degrees 旋转角度
         * @param px      旋转点横坐标
         * @param py      旋转点纵坐标
         * @return 旋转后的图片
         */
        public static Bitmap rotate(Bitmap src, int degrees, float px, float py) {
            return rotate(src, degrees, px, py, false);
        }

        /**
         * 旋转图片
         *
         * @param src     源图片
         * @param degrees 旋转角度
         * @param px      旋转点横坐标
         * @param py      旋转点纵坐标
         * @param recycle 是否回收
         * @return 旋转后的图片
         */
        public static Bitmap rotate(Bitmap src, int degrees, float px, float py, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            if (degrees == 0) return src;
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, px, py);
            Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 获取图片旋转角度
         *
         * @param filePath 文件路径
         * @return 旋转角度
         */
        public static int getRotateDegree(String filePath) {
            int degree = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(filePath);
                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    default:
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return degree;
        }

        /**
         * 转为圆形图片
         *
         * @param src 源图片
         * @return 圆形图片
         */
        public static Bitmap toRound(Bitmap src) {
            return toRound(src, false);
        }

        /**
         * 转为圆形图片
         *
         * @param src     源图片
         * @param recycle 是否回收
         * @return 圆形图片
         */
        public static Bitmap toRound(Bitmap src, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            int width = src.getWidth();
            int height = src.getHeight();
            int radius = Math.min(width, height) >> 1;
            Bitmap ret = src.copy(src.getConfig(), true);
            Paint paint = new Paint();
            Canvas canvas = new Canvas(ret);
            Rect rect = new Rect(0, 0, width, height);
            paint.setAntiAlias(true);
            paint.setColor(Color.TRANSPARENT);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawCircle(width >> 1, height >> 1, radius, paint);
            canvas.drawBitmap(src, rect, rect, paint);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 转为圆角图片
         *
         * @param src    源图片
         * @param radius 圆角的度数
         * @return 圆角图片
         */
        public static Bitmap toRoundCorner(Bitmap src, float radius) {
            return toRoundCorner(src, radius, false);
        }

        /**
         * 转为圆角图片
         *
         * @param src     源图片
         * @param radius  圆角的度数
         * @param recycle 是否回收
         * @return 圆角图片
         */
        public static Bitmap toRoundCorner(Bitmap src, float radius, boolean recycle) {
            if (null == src) return null;
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap ret = src.copy(src.getConfig(), true);
            BitmapShader bitmapShader = new BitmapShader(src,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            Canvas canvas = new Canvas(ret);
            RectF rectf = new RectF(0, 0, width, height);
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
            canvas.drawRoundRect(rectf, radius, radius, paint);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 快速模糊
         * <p>先缩小原图，对小图进行模糊，再放大回原先尺寸</p>
         *
         * @param context 上下文
         * @param src     源图片
         * @param scale   缩小倍数(0...1)
         * @param radius  模糊半径
         * @return 模糊后的图片
         */
        public static Bitmap fastBlur(Context context, Bitmap src, float scale, float radius) {
            return fastBlur(context, src, scale, radius, false);
        }

        /**
         * 快速模糊
         * <p>先缩小原图，对小图进行模糊，再放大回原先尺寸</p>
         *
         * @param context 上下文
         * @param src     源图片
         * @param scale   缩小倍数(0...1)
         * @param radius  模糊半径
         * @param recycle 是否回收
         * @return 模糊后的图片
         */
        public static Bitmap fastBlur(Context context, Bitmap src, float scale, float radius, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            int width = src.getWidth();
            int height = src.getHeight();
            int scaleWidth = (int) (width * scale + 0.5f);
            int scaleHeight = (int) (height * scale + 0.5f);
            if (scaleWidth == 0 || scaleHeight == 0) return null;
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(src, scaleWidth, scaleHeight, true);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas();
            PorterDuffColorFilter filter = new PorterDuffColorFilter(
                    Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
            paint.setColorFilter(filter);
            canvas.scale(scale, scale);
            canvas.drawBitmap(scaleBitmap, 0, 0, paint);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                scaleBitmap = renderScriptBlur(context, scaleBitmap, radius);
            } else {
                scaleBitmap = stackBlur(scaleBitmap, (int) radius, true);
            }
            if (scale == 1) return scaleBitmap;
            Bitmap ret = Bitmap.createScaledBitmap(scaleBitmap, width, height, true);
            if (scaleBitmap != null && !scaleBitmap.isRecycled()) scaleBitmap.recycle();
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * renderScript模糊图片
         * <p>API大于17</p>
         *
         * @param context 上下文
         * @param src     源图片
         * @param radius  模糊度(0...25)
         * @return 模糊后的图片
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public static Bitmap renderScriptBlur(Context context, Bitmap src, float radius) {
            if (isEmptyBitmap(src)) return null;
            RenderScript rs = null;
            try {
                rs = RenderScript.create(context);
                rs.setMessageHandler(new RenderScript.RSMessageHandler());
                Allocation input = Allocation.createFromBitmap(rs, src, Allocation.MipmapControl.MIPMAP_NONE, Allocation
                        .USAGE_SCRIPT);
                Allocation output = Allocation.createTyped(rs, input.getType());
                ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                if (radius > 25) {
                    radius = 25.0f;
                } else if (radius <= 0) {
                    radius = 1.0f;
                }
                blurScript.setInput(input);
                blurScript.setRadius(radius);
                blurScript.forEach(output);
                output.copyTo(src);
            } finally {
                if (rs != null) {
                    rs.destroy();
                }
            }
            return src;
        }

        /**
         * stack模糊图片
         *
         * @param src     源图片
         * @param radius  模糊半径
         * @param recycle 是否回收
         * @return stackBlur模糊图片
         */
        public static Bitmap stackBlur(Bitmap src, int radius, boolean recycle) {
            Bitmap ret;
            if (recycle) {
                ret = src;
            } else {
                ret = src.copy(src.getConfig(), true);
            }

            if (radius < 1) {
                return (null);
            }

            int w = ret.getWidth();
            int h = ret.getHeight();

            int[] pix = new int[w * h];
            ret.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }
            ret.setPixels(pix, 0, w, 0, 0, w, h);
            return (ret);
        }

        /**
         * 添加颜色边框
         *
         * @param src         源图片
         * @param borderWidth 边框宽度
         * @param color       边框的颜色值
         * @return 带颜色边框图
         */
        public static Bitmap addFrame(Bitmap src, int borderWidth, int color) {
            return addFrame(src, borderWidth, color);
        }

        /**
         * 添加颜色边框
         *
         * @param src         源图片
         * @param borderWidth 边框宽度
         * @param color       边框的颜色值
         * @param recycle     是否回收
         * @return 带颜色边框图
         */
        public static Bitmap addFrame(Bitmap src, int borderWidth, int color, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            int newWidth = src.getWidth() + borderWidth >> 1;
            int newHeight = src.getHeight() + borderWidth >> 1;
            Bitmap ret = Bitmap.createBitmap(newWidth, newHeight, src.getConfig());
            Canvas canvas = new Canvas(ret);
            Rect rec = canvas.getClipBounds();
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            canvas.drawRect(rec, paint);
            canvas.drawBitmap(src, borderWidth, borderWidth, null);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 添加倒影
         *
         * @param src              源图片的
         * @param reflectionHeight 倒影高度
         * @return 带倒影图片
         */
        public static Bitmap addReflection(Bitmap src, int reflectionHeight) {
            return addReflection(src, reflectionHeight, false);
        }

        /**
         * 添加倒影
         *
         * @param src              源图片的
         * @param reflectionHeight 倒影高度
         * @param recycle          是否回收
         * @return 带倒影图片
         */
        public static Bitmap addReflection(Bitmap src, int reflectionHeight, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            final int REFLECTION_GAP = 0;
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();
            if (0 == srcWidth || srcHeight == 0) return null;
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            Bitmap reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight,
                    srcWidth, reflectionHeight, matrix, false);
            if (null == reflectionBitmap) return null;
            Bitmap ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
            Canvas canvas = new Canvas(ret);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    ret.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));
            canvas.save();
            canvas.drawRect(0, srcHeight, srcWidth,
                    ret.getHeight() + REFLECTION_GAP, paint);
            canvas.restore();
            if (!reflectionBitmap.isRecycled()) reflectionBitmap.recycle();
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 添加文字水印
         *
         * @param src      源图片
         * @param content  水印文本
         * @param textSize 水印字体大小
         * @param color    水印字体颜色
         * @param alpha    水印字体透明度
         * @param x        起始坐标x
         * @param y        起始坐标y
         * @return 带有文字水印的图片
         */
        public static Bitmap addTextWatermark(Bitmap src, String content, int textSize, int color, int alpha, float x, float y) {
            return addTextWatermark(src, content, textSize, color, alpha, x, y, false);
        }

        /**
         * 添加文字水印
         *
         * @param src      源图片
         * @param content  水印文本
         * @param textSize 水印字体大小
         * @param color    水印字体颜色
         * @param alpha    水印字体透明度
         * @param x        起始坐标x
         * @param y        起始坐标y
         * @param recycle  是否回收
         * @return 带有文字水印的图片
         */
        public static Bitmap addTextWatermark(Bitmap src, String content, int textSize, int color, int alpha, float x, float y, boolean recycle) {
            if (isEmptyBitmap(src) || content == null) return null;
            Bitmap ret = src.copy(src.getConfig(), true);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);
            paint.setColor(color);
            paint.setTextSize(textSize);
            Rect bounds = new Rect();
            paint.getTextBounds(content, 0, content.length(), bounds);
            canvas.drawText(content, x, y, paint);
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 添加图片水印
         *
         * @param src       源图片
         * @param watermark 图片水印
         * @param x         起始坐标x
         * @param y         起始坐标y
         * @param alpha     透明度
         * @return 带有图片水印的图片
         */
        public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha) {
            return addImageWatermark(src, watermark, x, y, alpha, false);
        }

        /**
         * 添加图片水印
         *
         * @param src       源图片
         * @param watermark 图片水印
         * @param x         起始坐标x
         * @param y         起始坐标y
         * @param alpha     透明度
         * @param recycle   是否回收
         * @return 带有图片水印的图片
         */
        public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Bitmap ret = src.copy(src.getConfig(), true);
            if (!isEmptyBitmap(watermark)) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                Canvas canvas = new Canvas(ret);
                paint.setAlpha(alpha);
                canvas.drawBitmap(watermark, x, y, paint);
            }
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 转为alpha位图
         *
         * @param src 源图片
         * @return alpha位图
         */
        public static Bitmap toAlpha(Bitmap src) {
            return toAlpha(src);
        }

        /**
         * 转为alpha位图
         *
         * @param src     源图片
         * @param recycle 是否回收
         * @return alpha位图
         */
        public static Bitmap toAlpha(Bitmap src, Boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Bitmap ret = src.extractAlpha();
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * 转为灰度图片
         *
         * @param src 源图片
         * @return 灰度图
         */
        public static Bitmap toGray(Bitmap src) {
            return toGray(src, false);
        }

        /**
         * 转为灰度图片
         *
         * @param src     源图片
         * @param recycle 是否回收
         * @return 灰度图
         */
        public static Bitmap toGray(Bitmap src, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            Bitmap grayBitmap = Bitmap.createBitmap(src.getWidth(),
                    src.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(grayBitmap);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
            paint.setColorFilter(colorMatrixColorFilter);
            canvas.drawBitmap(src, 0, 0, paint);
            if (recycle && !src.isRecycled()) src.recycle();
            return grayBitmap;
        }

        /**
         * 保存图片
         *
         * @param src      源图片
         * @param filePath 要保存到的文件路径
         * @param format   格式
         * @return {@code true}: 成功<br>{@code false}: 失败
         */
        public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format) {
            return save(src, FileUtils.getFileByPath(filePath), format, false);
        }

        /**
         * 保存图片
         *
         * @param src    源图片
         * @param file   要保存到的文件
         * @param format 格式
         * @return {@code true}: 成功<br>{@code false}: 失败
         */
        public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format) {
            return save(src, file, format, false);
        }

        /**
         * 保存图片
         *
         * @param src      源图片
         * @param filePath 要保存到的文件路径
         * @param format   格式
         * @param recycle  是否回收
         * @return {@code true}: 成功<br>{@code false}: 失败
         */
        public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format, boolean recycle) {
            return save(src, FileUtils.getFileByPath(filePath), format, recycle);
        }

        /**
         * 保存图片
         *
         * @param src     源图片
         * @param file    要保存到的文件
         * @param format  格式
         * @param recycle 是否回收
         * @return {@code true}: 成功<br>{@code false}: 失败
         */
        public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
            if (isEmptyBitmap(src) || !FileUtils.createOrExistsFile(file)) return false;
            System.out.println(src.getWidth() + ", " + src.getHeight());
            OutputStream os = null;
            boolean ret = false;
            try {
                os = new BufferedOutputStream(new FileOutputStream(file));
                ret = src.compress(format, 100, os);
                if (recycle && !src.isRecycled()) src.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.closeIO(os);
            }
            return ret;
        }

        /**
         * 根据文件名判断文件是否为图片
         *
         * @param file 　文件
         */
        public static boolean isImage(File file) {
            return file != null && isImage(file.getPath());
        }

        /**
         * 根据文件名判断文件是否为图片
         *
         * @param filePath 　文件路径
         */
        public static boolean isImage(String filePath) {
            String path = filePath.toUpperCase();
            return path.endsWith(".PNG") || path.endsWith(".JPG")
                    || path.endsWith(".JPEG") || path.endsWith(".BMP")
                    || path.endsWith(".GIF");
        }

        /**
         * 获取图片类型
         *
         * @param filePath 文件路径
         * @return 图片类型
         */
        public static String getImageType(String filePath) {
            return getImageType(FileUtils.getFileByPath(filePath));
        }

        /**
         * 获取图片类型
         *
         * @param file 文件
         * @return 图片类型
         */
        public static String getImageType(File file) {
            if (file == null) return null;
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                return getImageType(is);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                FileUtils.closeIO(is);
            }
        }

        /**
         * 流获取图片类型
         *
         * @param is 图片输入流
         * @return 图片类型
         */
        public static String getImageType(InputStream is) {
            if (is == null) return null;
            try {
                byte[] bytes = new byte[8];
                return is.read(bytes, 0, 8) != -1 ? getImageType(bytes) : null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 获取图片类型
         *
         * @param bytes bitmap的前8字节
         * @return 图片类型
         */
        public static String getImageType(byte[] bytes) {
            if (isJPEG(bytes)) return "JPEG";
            if (isGIF(bytes)) return "GIF";
            if (isPNG(bytes)) return "PNG";
            if (isBMP(bytes)) return "BMP";
            return null;
        }

        private static boolean isJPEG(byte[] b) {
            return b.length >= 2
                    && (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
        }

        private static boolean isGIF(byte[] b) {
            return b.length >= 6
                    && b[0] == 'G' && b[1] == 'I'
                    && b[2] == 'F' && b[3] == '8'
                    && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
        }

        private static boolean isPNG(byte[] b) {
            return b.length >= 8
                    && (b[0] == (byte) 137 && b[1] == (byte) 80
                    && b[2] == (byte) 78 && b[3] == (byte) 71
                    && b[4] == (byte) 13 && b[5] == (byte) 10
                    && b[6] == (byte) 26 && b[7] == (byte) 10);
        }

        private static boolean isBMP(byte[] b) {
            return b.length >= 2
                    && (b[0] == 0x42) && (b[1] == 0x4d);
        }

        /**
         * 判断bitmap对象是否为空
         *
         * @param src 源图片
         * @return {@code true}: 是<br>{@code false}: 否
         */
        private static boolean isEmptyBitmap(Bitmap src) {
            return src == null || src.getWidth() == 0 || src.getHeight() == 0;
        }

        /******************************~~~~~~~~~ 下方和压缩有关 ~~~~~~~~~******************************/

        /**
         * 按缩放压缩
         *
         * @param src       源图片
         * @param newWidth  新宽度
         * @param newHeight 新高度
         * @return 缩放压缩后的图片
         */
        public static Bitmap compressByScale(Bitmap src, int newWidth, int newHeight) {
            return scale(src, newWidth, newHeight, false);
        }

        /**
         * 按缩放压缩
         *
         * @param src       源图片
         * @param newWidth  新宽度
         * @param newHeight 新高度
         * @return 缩放压缩后的图片
         */
        public static Bitmap compressByScale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
            return scale(src, newWidth, newHeight, recycle);
        }

        /**
         * 按缩放压缩
         *
         * @param src         源图片
         * @param scaleWidth  缩放宽度倍数
         * @param scaleHeight 缩放高度倍数
         * @return 缩放压缩后的图片
         */
        public static Bitmap compressByScale(Bitmap src, float scaleWidth, float scaleHeight) {
            return scale(src, scaleWidth, scaleHeight, false);
        }

        /**
         * 按缩放压缩
         *
         * @param src         源图片
         * @param scaleWidth  缩放宽度倍数
         * @param scaleHeight 缩放高度倍数
         * @param recycle     是否回收
         * @return 缩放压缩后的图片
         */
        public static Bitmap compressByScale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
            return scale(src, scaleWidth, scaleHeight, recycle);
        }

        /**
         * 按质量压缩
         *
         * @param src     源图片
         * @param quality 质量
         * @return 质量压缩后的图片
         */
        public static Bitmap compressByQuality(Bitmap src, int quality) {
            return compressByQuality(src, quality, false);
        }

        /**
         * 按质量压缩
         *
         * @param src     源图片
         * @param quality 质量
         * @param recycle 是否回收
         * @return 质量压缩后的图片
         */
        public static Bitmap compressByQuality(Bitmap src, int quality, boolean recycle) {
            if (isEmptyBitmap(src) || quality < 0 || quality > 100) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            byte[] bytes = baos.toByteArray();
            if (recycle && !src.isRecycled()) src.recycle();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        /**
         * 按质量压缩
         *
         * @param src         源图片
         * @param maxByteSize 允许最大值字节数
         * @return 质量压缩压缩过的图片
         */
        public static Bitmap compressByQuality(Bitmap src, long maxByteSize) {
            return compressByQuality(src, maxByteSize, false);
        }

        /**
         * 按质量压缩
         *
         * @param src         源图片
         * @param maxByteSize 允许最大值字节数
         * @param recycle     是否回收
         * @return 质量压缩压缩过的图片
         */
        public static Bitmap compressByQuality(Bitmap src, long maxByteSize, boolean recycle) {
            if (isEmptyBitmap(src) || maxByteSize <= 0) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            src.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            while (baos.toByteArray().length > maxByteSize && quality >= 0) {
                baos.reset();
                src.compress(Bitmap.CompressFormat.JPEG, quality -= 5, baos);
            }
            if (quality < 0) return null;
            byte[] bytes = baos.toByteArray();
            if (recycle && !src.isRecycled()) src.recycle();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        /**
         * 按采样大小压缩
         *
         * @param src        源图片
         * @param sampleSize 采样率大小
         * @return 按采样率压缩后的图片
         */
        public static Bitmap compressBySampleSize(Bitmap src, int sampleSize) {
            return compressBySampleSize(src, sampleSize, false);
        }

        /**
         * 按采样大小压缩
         *
         * @param src        源图片
         * @param sampleSize 采样率大小
         * @param recycle    是否回收
         * @return 按采样率压缩后的图片
         */
        public static Bitmap compressBySampleSize(Bitmap src, int sampleSize, boolean recycle) {
            if (isEmptyBitmap(src)) return null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            if (recycle && !src.isRecycled()) src.recycle();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/1
     *     desc  : 设备相关工具类
     * </pre>
     */
    public static class DeviceUtils {

        private DeviceUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 获取设备MAC地址
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
         *
         * @param context 上下文
         * @return MAC地址
         */
        public static String getMacAddress(Context context) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null) {
                String macAddress = info.getMacAddress();
                if (macAddress != null) {
                    return macAddress.replace(":", "");
                }
            }
            return null;
        }

        /**
         * 获取设备MAC地址
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
         *
         * @return MAC地址
         */

        public static String getMacAddress() {
            String macAddress = null;
            LineNumberReader lnr = null;
            InputStreamReader isr = null;
            try {
                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
                isr = new InputStreamReader(pp.getInputStream());
                lnr = new LineNumberReader(isr);
                macAddress = lnr.readLine().replace(":", "");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.closeIO(lnr, isr);
            }
            return macAddress == null ? "" : macAddress;
        }

        /**
         * 获取设备厂商，如Xiaomi
         *
         * @return 设备厂商
         */
        public static String getManufacturer() {
            return Build.MANUFACTURER;
        }

        /**
         * 获取设备型号，如MI2SC
         *
         * @return 设备型号
         */
        public static String getModel() {
            String model = Build.MODEL;
            if (model != null) {
                model = model.trim().replaceAll("\\s*", "");
            } else {
                model = "";
            }
            return model;
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/7
     *     desc  : 编码解码相关工具类
     * </pre>
     */
    public static class EncodeUtils {

        private EncodeUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * URL编码
         * <p>若想自己指定字符集,可以使用{@link #urlEncode(String input, String charset)}方法</p>
         *
         * @param input 要编码的字符
         * @return 编码为UTF-8的字符串
         */
        public static String urlEncode(String input) {
            return urlEncode(input, "UTF-8");
        }

        /**
         * URL编码
         * <p>若系统不支持指定的编码字符集,则直接将input原样返回</p>
         *
         * @param input   要编码的字符
         * @param charset 字符集
         * @return 编码为字符集的字符串
         */
        public static String urlEncode(String input, String charset) {
            try {
                return URLEncoder.encode(input, charset);
            } catch (UnsupportedEncodingException e) {
                return input;
            }
        }

        /**
         * URL解码
         * <p>若想自己指定字符集,可以使用 {@link #urlDecode(String input, String charset)}方法</p>
         *
         * @param input 要解码的字符串
         * @return URL解码后的字符串
         */
        public static String urlDecode(String input) {
            return urlDecode(input, "UTF-8");
        }

        /**
         * URL解码
         * <p>若系统不支持指定的解码字符集,则直接将input原样返回</p>
         *
         * @param input   要解码的字符串
         * @param charset 字符集
         * @return URL解码为指定字符集的字符串
         */
        public static String urlDecode(String input, String charset) {
            try {
                return URLDecoder.decode(input, charset);
            } catch (UnsupportedEncodingException e) {
                return input;
            }
        }

        /**
         * Base64编码
         *
         * @param input 要编码的字符串
         * @return Base64编码后的字符串
         */
        public static byte[] base64Encode(String input) {
            return base64Encode(input.getBytes());
        }

        /**
         * Base64编码
         *
         * @param input 要编码的字节数组
         * @return Base64编码后的字符串
         */
        public static byte[] base64Encode(byte[] input) {
            return Base64.encode(input, Base64.NO_WRAP);
        }

        /**
         * Base64编码
         *
         * @param input 要编码的字节数组
         * @return Base64编码后的字符串
         */
        public static String base64Encode2String(byte[] input) {
            return Base64.encodeToString(input, Base64.NO_WRAP);
        }

        /**
         * Base64解码
         *
         * @param input 要解码的字符串
         * @return Base64解码后的字符串
         */
        public static byte[] base64Decode(String input) {
            return Base64.decode(input, Base64.NO_WRAP);
        }

        /**
         * Base64解码
         *
         * @param input 要解码的字符串
         * @return Base64解码后的字符串
         */
        public static byte[] base64Decode(byte[] input) {
            return Base64.decode(input, Base64.NO_WRAP);
        }

        /**
         * Base64URL安全编码
         * <p>将Base64中的URL非法字符�?,/=转为其他字符, 见RFC3548</p>
         *
         * @param input 要Base64URL安全编码的字符串
         * @return Base64URL安全编码后的字符串
         */
        public static byte[] base64UrlSafeEncode(String input) {
            return Base64.encode(input.getBytes(), Base64.URL_SAFE);
        }

        /**
         * Html编码
         *
         * @param input 要Html编码的字符串
         * @return Html编码后的字符串
         */
        public static String htmlEncode(String input) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return Html.escapeHtml(input);
            } else {
                // 参照Html.escapeHtml()中代码
                StringBuilder out = new StringBuilder();
                for (int i = 0, len = input.length(); i < len; i++) {
                    char c = input.charAt(i);
                    if (c == '<') {
                        out.append("&lt;");
                    } else if (c == '>') {
                        out.append("&gt;");
                    } else if (c == '&') {
                        out.append("&amp;");
                    } else if (c >= 0xD800 && c <= 0xDFFF) {
                        if (c < 0xDC00 && i + 1 < len) {
                            char d = input.charAt(i + 1);
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                i++;
                                int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                                out.append("&#").append(codepoint).append(";");
                            }
                        }
                    } else if (c > 0x7E || c < ' ') {
                        out.append("&#").append((int) c).append(";");
                    } else if (c == ' ') {
                        while (i + 1 < len && input.charAt(i + 1) == ' ') {
                            out.append("&nbsp;");
                            i++;
                        }
                        out.append(' ');
                    } else {
                        out.append(c);
                    }
                }
                return out.toString();
            }
        }

        /**
         * Html解码
         *
         * @param input 待解码的字符串
         * @return Html解码后的字符串
         */
        public static String htmlDecode(String input) {
            return Html.fromHtml(input).toString();
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 加密解密相关的工具类
     * </pre>
     */
    public static class EncryptUtils {

        private EncryptUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /*********************** 哈希加密相关 ***********************/
        /**
         * MD2加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptMD2ToString(String data) {
            return encryptMD2ToString(data.getBytes());
        }

        /**
         * MD2加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptMD2ToString(byte[] data) {
            return bytes2HexString(encryptMD2(data));
        }

        /**
         * MD2加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptMD2(byte[] data) {
            return encryptAlgorithm(data, "MD2");
        }

        /**
         * MD5加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptMD5ToString(String data) {
            return encryptMD5ToString(data.getBytes());
        }

        /**
         * MD5加密
         *
         * @param data 明文字符串
         * @param salt 盐
         * @return 16进制加盐密文
         */
        public static String encryptMD5ToString(String data, String salt) {
            return bytes2HexString(encryptMD5((data + salt).getBytes()));
        }

        /**
         * MD5加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptMD5ToString(byte[] data) {
            return bytes2HexString(encryptMD5(data));
        }

        /**
         * MD5加密
         *
         * @param data 明文字节数组
         * @param salt 盐字节数组
         * @return 16进制加盐密文
         */
        public static String encryptMD5ToString(byte[] data, byte[] salt) {
            byte[] dataSalt = new byte[data.length + salt.length];
            System.arraycopy(data, 0, dataSalt, 0, data.length);
            System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
            return bytes2HexString(encryptMD5(dataSalt));
        }

        /**
         * MD5加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptMD5(byte[] data) {
            return encryptAlgorithm(data, "MD5");
        }

        /**
         * MD5加密文件
         *
         * @param filePath 文件路径
         * @return 文件的16进制密文
         */
        public static String encryptMD5File2String(String filePath) {
            return encryptMD5File2String(new File(filePath));
        }

        /**
         * MD5加密文件
         *
         * @param filePath 文件路径
         * @return 文件的MD5校验码
         */
        public static byte[] encryptMD5File(String filePath) {
            return encryptMD5File(new File(filePath));
        }

        /**
         * MD5加密文件
         *
         * @param file 文件
         * @return 文件的16进制密文
         */
        public static String encryptMD5File2String(File file) {
            return encryptMD5File(file) != null ? bytes2HexString(encryptMD5File(file)) : "";
        }

        /**
         * MD5加密文件
         *
         * @param file 文件
         * @return 文件的MD5校验码
         */
        public static byte[] encryptMD5File(File file) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                FileChannel channel = fis.getChannel();
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(buffer);
                return md.digest();
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.closeIO(fis);
            }
            return null;
        }

        /**
         * SHA1加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptSHA1ToString(String data) {
            return encryptSHA1ToString(data.getBytes());
        }

        /**
         * SHA1加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptSHA1ToString(byte[] data) {
            return bytes2HexString(encryptSHA1(data));
        }

        /**
         * SHA1加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptSHA1(byte[] data) {
            return encryptAlgorithm(data, "SHA-1");
        }

        /**
         * SHA224加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptSHA224ToString(String data) {
            return encryptSHA224ToString(data.getBytes());
        }

        /**
         * SHA224加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptSHA224ToString(byte[] data) {
            return bytes2HexString(encryptSHA224(data));
        }

        /**
         * SHA224加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptSHA224(byte[] data) {
            return encryptAlgorithm(data, "SHA-224");
        }

        /**
         * SHA256加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptSHA256ToString(String data) {
            return encryptSHA256ToString(data.getBytes());
        }

        /**
         * SHA256加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptSHA256ToString(byte[] data) {
            return bytes2HexString(encryptSHA256(data));
        }

        /**
         * SHA256加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptSHA256(byte[] data) {
            return encryptAlgorithm(data, "SHA-256");
        }

        /**
         * SHA384加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptSHA384ToString(String data) {
            return encryptSHA384ToString(data.getBytes());
        }

        /**
         * SHA384加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptSHA384ToString(byte[] data) {
            return bytes2HexString(encryptSHA384(data));
        }

        /**
         * SHA384加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptSHA384(byte[] data) {
            return encryptAlgorithm(data, "SHA-384");
        }

        /**
         * SHA512加密
         *
         * @param data 明文字符串
         * @return 16进制密文
         */
        public static String encryptSHA512ToString(String data) {
            return encryptSHA512ToString(data.getBytes());
        }

        /**
         * SHA512加密
         *
         * @param data 明文字节数组
         * @return 16进制密文
         */
        public static String encryptSHA512ToString(byte[] data) {
            return bytes2HexString(encryptSHA512(data));
        }

        /**
         * SHA512加密
         *
         * @param data 明文字节数组
         * @return 密文字节数组
         */
        public static byte[] encryptSHA512(byte[] data) {
            return encryptAlgorithm(data, "SHA-512");
        }

        /**
         * 对data进行algorithm算法加密
         *
         * @param data      明文字节数组
         * @param algorithm 加密算法
         * @return 密文字节数组
         */
        private static byte[] encryptAlgorithm(byte[] data, String algorithm) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(data);
                return md.digest();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return new byte[0];
        }

        /************************ DES加密相关 ***********************/
        /**
         * DES转变
         * <p>法算法名称/加密模式/填充方式</p>
         * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
         * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
         */
        public static String DES_Transformation = "DES/ECB/NoPadding";
        private static final String DES_Algorithm = "DES";

        /**
         * @param data           数据
         * @param key            秘钥
         * @param algorithm      采用何种DES算法
         * @param transformation 转变
         * @param isEncrypt      是否加密
         * @return 密文或者明文，适用于DES，3DES，AES
         */
        public static byte[] DESTemplet(byte[] data, byte[] key, String algorithm, String transformation, boolean isEncrypt) {
            try {
                SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
                Cipher cipher = Cipher.getInstance(transformation);
                SecureRandom random = new SecureRandom();
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, random);
                return cipher.doFinal(data);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * DES加密后转为Base64编码
         *
         * @param data 明文
         * @param key  8字节秘钥
         * @return Base64密文
         */
        public static byte[] encryptDES2Base64(byte[] data, byte[] key) {
            return com.blankj.utilcode.utils.EncodeUtils.base64Encode(encryptDES(data, key));
        }

        /**
         * DES加密后转为16进制
         *
         * @param data 明文
         * @param key  8字节秘钥
         * @return 16进制密文
         */
        public static String encryptDES2HexString(byte[] data, byte[] key) {
            return bytes2HexString(encryptDES(data, key));
        }

        /**
         * DES加密
         *
         * @param data 明文
         * @param key  8字节秘钥
         * @return 密文
         */
        public static byte[] encryptDES(byte[] data, byte[] key) {
            return DESTemplet(data, key, DES_Algorithm, DES_Transformation, true);
        }

        /**
         * DES解密Base64编码密文
         *
         * @param data Base64编码密文
         * @param key  8字节秘钥
         * @return 明文
         */
        public static byte[] decryptBase64DES(byte[] data, byte[] key) {
            return decryptDES(com.blankj.utilcode.utils.EncodeUtils.base64Decode(data), key);
        }

        /**
         * DES解密16进制密文
         *
         * @param data 16进制密文
         * @param key  8字节秘钥
         * @return 明文
         */
        public static byte[] decryptHexStringDES(String data, byte[] key) {
            return decryptDES(hexString2Bytes(data), key);
        }

        /**
         * DES解密
         *
         * @param data 密文
         * @param key  8字节秘钥
         * @return 明文
         */
        public static byte[] decryptDES(byte[] data, byte[] key) {
            return DESTemplet(data, key, DES_Algorithm, DES_Transformation, false);
        }

        /************************ 3DES加密相关 ***********************/
        /**
         * 3DES转变
         * <p>法算法名称/加密模式/填充方式</p>
         * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
         * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
         */
        public static String TripleDES_Transformation = "DESede/ECB/NoPadding";
        private static final String TripleDES_Algorithm = "DESede";


        /**
         * 3DES加密后转为Base64编码
         *
         * @param data 明文
         * @param key  24字节秘钥
         * @return Base64密文
         */
        public static byte[] encrypt3DES2Base64(byte[] data, byte[] key) {
            return com.blankj.utilcode.utils.EncodeUtils.base64Encode(encrypt3DES(data, key));
        }

        /**
         * 3DES加密后转为16进制
         *
         * @param data 明文
         * @param key  24字节秘钥
         * @return 16进制密文
         */
        public static String encrypt3DES2HexString(byte[] data, byte[] key) {
            return bytes2HexString(encrypt3DES(data, key));
        }

        /**
         * 3DES加密
         *
         * @param data 明文
         * @param key  24字节密钥
         * @return 密文
         */
        public static byte[] encrypt3DES(byte[] data, byte[] key) {
            return DESTemplet(data, key, TripleDES_Algorithm, TripleDES_Transformation, true);
        }

        /**
         * 3DES解密Base64编码密文
         *
         * @param data Base64编码密文
         * @param key  24字节秘钥
         * @return 明文
         */
        public static byte[] decryptBase64_3DES(byte[] data, byte[] key) {
            return decrypt3DES(com.blankj.utilcode.utils.EncodeUtils.base64Decode(data), key);
        }

        /**
         * 3DES解密16进制密文
         *
         * @param data 16进制密文
         * @param key  24字节秘钥
         * @return 明文
         */
        public static byte[] decryptHexString3DES(String data, byte[] key) {
            return decrypt3DES(hexString2Bytes(data), key);
        }

        /**
         * 3DES解密
         *
         * @param data 密文
         * @param key  24字节密钥
         * @return 明文
         */
        public static byte[] decrypt3DES(byte[] data, byte[] key) {
            return DESTemplet(data, key, TripleDES_Algorithm, TripleDES_Transformation, false);
        }

        /************************ AES加密相关 ***********************/
        /**
         * AES转变
         * <p>法算法名称/加密模式/填充方式</p>
         * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
         * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
         */
        public static String AES_Transformation = "AES/ECB/NoPadding";
        private static final String AES_Algorithm = "AES";


        /**
         * AES加密后转为Base64编码
         *
         * @param data 明文
         * @param key  16、24、32字节秘钥
         * @return Base64密文
         */
        public static byte[] encryptAES2Base64(byte[] data, byte[] key) {
            return com.blankj.utilcode.utils.EncodeUtils.base64Encode(encryptAES(data, key));
        }

        /**
         * AES加密后转为16进制
         *
         * @param data 明文
         * @param key  16、24、32字节秘钥
         * @return 16进制密文
         */
        public static String encryptAES2HexString(byte[] data, byte[] key) {
            return bytes2HexString(encryptAES(data, key));
        }

        /**
         * AES加密
         *
         * @param data 明文
         * @param key  16、24、32字节秘钥
         * @return 密文
         */
        public static byte[] encryptAES(byte[] data, byte[] key) {
            return DESTemplet(data, key, AES_Algorithm, AES_Transformation, true);
        }

        /**
         * AES解密Base64编码密文
         *
         * @param data Base64编码密文
         * @param key  16、24、32字节秘钥
         * @return 明文
         */
        public static byte[] decryptBase64AES(byte[] data, byte[] key) {
            return decryptAES(com.blankj.utilcode.utils.EncodeUtils.base64Decode(data), key);
        }

        /**
         * AES解密16进制密文
         *
         * @param data 16进制密文
         * @param key  16、24、32字节秘钥
         * @return 明文
         */
        public static byte[] decryptHexStringAES(String data, byte[] key) {
            return decryptAES(hexString2Bytes(data), key);
        }

        /**
         * AES解密
         *
         * @param data 密文
         * @param key  16、24、32字节秘钥
         * @return 明文
         */
        public static byte[] decryptAES(byte[] data, byte[] key) {
            return DESTemplet(data, key, AES_Algorithm, AES_Transformation, false);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/11
     *     desc  : 文件相关工具类
     * </pre>
     */
    public static class FileUtils {

        private FileUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 根据文件路径获取文件
         *
         * @param filePath 文件路径
         * @return 文件
         */
        public static File getFileByPath(String filePath) {
            return StringUtils.isSpace(filePath) ? null : new File(filePath);
        }

        /**
         * 判断文件是否存在
         *
         * @param filePath 文件路径
         * @return {@code true}: 存在<br>{@code false}: 不存在
         */
        public static boolean isFileExists(String filePath) {
            return isFileExists(getFileByPath(filePath));
        }

        /**
         * 判断文件是否存在
         *
         * @param file 文件
         * @return {@code true}: 存在<br>{@code false}: 不存在
         */
        public static boolean isFileExists(File file) {
            return file != null && file.exists();
        }

        /**
         * 判断是否是目录
         *
         * @param dirPath 目录路径
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isDir(String dirPath) {
            return isDir(getFileByPath(dirPath));
        }

        /**
         * 判断是否是目录
         *
         * @param file 文件
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isDir(File file) {
            return isFileExists(file) && file.isDirectory();
        }

        /**
         * 判断是否是文件
         *
         * @param filePath 文件路径
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isFile(String filePath) {
            return isFile(getFileByPath(filePath));
        }

        /**
         * 判断是否是文件
         *
         * @param file 文件
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isFile(File file) {
            return isFileExists(file) && file.isFile();
        }

        /**
         * 判断目录是否存在，不存在则判断是否创建成功
         *
         * @param dirPath 文件路径
         * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
         */
        public static boolean createOrExistsDir(String dirPath) {
            return createOrExistsDir(getFileByPath(dirPath));
        }

        /**
         * 判断目录是否存在，不存在则判断是否创建成功
         *
         * @param file 文件
         * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
         */
        public static boolean createOrExistsDir(File file) {
            // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
            return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
        }

        /**
         * 判断文件是否存在，不存在则判断是否创建成功
         *
         * @param filePath 文件路径
         * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
         */
        public static boolean createOrExistsFile(String filePath) {
            return createOrExistsFile(getFileByPath(filePath));
        }

        /**
         * 判断文件是否存在，不存在则判断是否创建成功
         *
         * @param file 文件
         * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
         */
        public static boolean createOrExistsFile(File file) {
            if (file == null) return false;
            // 如果存在，是文件则返回true，是目录则返回false
            if (file.exists()) return file.isFile();
            if (!createOrExistsDir(file.getParentFile())) return false;
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 判断文件是否存在，存在则在创建之前删除
         *
         * @param filePath 文件路径
         * @return {@code true}: 创建成功<br>{@code false}: 创建失败
         */
        public static boolean createFileByDeleteOldFile(String filePath) {
            return createFileByDeleteOldFile(getFileByPath(filePath));
        }

        /**
         * 判断文件是否存在，存在则在创建之前删除
         *
         * @param file 文件
         * @return {@code true}: 创建成功<br>{@code false}: 创建失败
         */
        public static boolean createFileByDeleteOldFile(File file) {
            if (file == null) return false;
            // 文件存在并且删除失败返回false
            if (file.exists() && file.isFile() && !file.delete()) return false;
            // 创建目录失败返回false
            if (!createOrExistsDir(file.getParentFile())) return false;
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 复制或移动目录
         *
         * @param srcDirPath  源目录路径
         * @param destDirPath 目标目录路径
         * @param isMove      是否移动
         * @return {@code true}: 复制或移动成功<br>{@code false}: 复制或移动失败
         */
        private static boolean copyOrMoveDir(String srcDirPath, String destDirPath, boolean isMove) {
            return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove);
        }

        /**
         * 复制或移动目录
         *
         * @param srcDir  源目录
         * @param destDir 目标目录
         * @param isMove  是否移动
         * @return {@code true}: 复制或移动成功<br>{@code false}: 复制或移动失败
         */
        private static boolean copyOrMoveDir(File srcDir, File destDir, boolean isMove) {
            if (srcDir == null || destDir == null) return false;
            // 如果目标目录在源目录中则返回false，看不懂的话好好想想递归怎么结束
            // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
            // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
            // 为防止以上这种情况出现出现误判，须分别在后面加个路径分隔符
            String srcPath = srcDir.getPath() + File.separator;
            String destPath = destDir.getPath() + File.separator;
            if (destPath.contains(srcPath)) return false;
            // 源文件不存在或者不是目录则返回false
            if (!srcDir.exists() || !srcDir.isDirectory()) return false;
            // 目标目录不存在返回false
            if (!createOrExistsDir(destDir)) return false;
            File[] files = srcDir.listFiles();
            for (File file : files) {
                File oneDestFile = new File(destPath + file.getName());
                if (file.isFile()) {
                    // 如果操作失败返回false
                    if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
                } else if (file.isDirectory()) {
                    // 如果操作失败返回false
                    if (!copyOrMoveDir(file, oneDestFile, isMove)) return false;
                }
            }
            return !isMove || deleteDir(srcDir);
        }

        /**
         * 复制或移动文件
         *
         * @param srcFilePath  源文件路径
         * @param destFilePath 目标文件路径
         * @param isMove       是否移动
         * @return {@code true}: 复制或移动成功<br>{@code false}: 复制或移动失败
         */
        private static boolean copyOrMoveFile(String srcFilePath, String destFilePath, boolean isMove) {
            return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove);
        }

        /**
         * 复制或移动文件
         *
         * @param srcFile  源文件
         * @param destFile 目标文件
         * @param isMove   是否移动
         * @return {@code true}: 复制或移动成功<br>{@code false}: 复制或移动失败
         */
        private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
            if (srcFile == null || destFile == null) return false;
            // 源文件不存在或者不是文件则返回false
            if (!srcFile.exists() || !srcFile.isFile()) return false;
            // 目标文件存在且是文件则返回false
            if (destFile.exists() && destFile.isFile()) return false;
            // 目标目录不存在返回false
            if (!createOrExistsDir(destFile.getParentFile())) return false;
            try {
                return writeFileFromIS(destFile, new FileInputStream(srcFile), false)
                        && !(isMove && !deleteFile(srcFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 复制目录
         *
         * @param srcDirPath  源目录路径
         * @param destDirPath 目标目录路径
         * @return {@code true}: 复制成功<br>{@code false}: 复制失败
         */
        public static boolean copyDir(String srcDirPath, String destDirPath) {
            return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
        }

        /**
         * 复制目录
         *
         * @param srcDir  源目录
         * @param destDir 目标目录
         * @return {@code true}: 复制成功<br>{@code false}: 复制失败
         */
        public static boolean copyDir(File srcDir, File destDir) {
            return copyOrMoveDir(srcDir, destDir, false);
        }

        /**
         * 复制文件
         *
         * @param srcFilePath  源文件路径
         * @param destFilePath 目标文件路径
         * @return {@code true}: 复制成功<br>{@code false}: 复制失败
         */
        public static boolean copyFile(String srcFilePath, String destFilePath) {
            return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
        }

        /**
         * 复制文件
         *
         * @param srcFile  源文件
         * @param destFile 目标文件
         * @return {@code true}: 复制成功<br>{@code false}: 复制失败
         */
        public static boolean copyFile(File srcFile, File destFile) {
            return copyOrMoveFile(srcFile, destFile, false);
        }

        /**
         * 移动目录
         *
         * @param srcDirPath  源目录路径
         * @param destDirPath 目标目录路径
         * @return {@code true}: 移动成功<br>{@code false}: 移动失败
         */
        public static boolean moveDir(String srcDirPath, String destDirPath) {
            return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
        }

        /**
         * 移动目录
         *
         * @param srcDir  源目录
         * @param destDir 目标目录
         * @return {@code true}: 移动成功<br>{@code false}: 移动失败
         */
        public static boolean moveDir(File srcDir, File destDir) {
            return copyOrMoveDir(srcDir, destDir, true);
        }

        /**
         * 移动文件
         *
         * @param srcFilePath  源文件路径
         * @param destFilePath 目标文件路径
         * @return {@code true}: 移动成功<br>{@code false}: 移动失败
         */
        public static boolean moveFile(String srcFilePath, String destFilePath) {
            return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
        }

        /**
         * 移动文件
         *
         * @param srcFile  源文件
         * @param destFile 目标文件
         * @return {@code true}: 移动成功<br>{@code false}: 移动失败
         */
        public static boolean moveFile(File srcFile, File destFile) {
            return copyOrMoveFile(srcFile, destFile, true);
        }

        /**
         * 删除目录
         *
         * @param dirPath 目录路径
         * @return {@code true}: 删除成功<br>{@code false}: 删除失败
         */
        public static boolean deleteDir(String dirPath) {
            return deleteDir(getFileByPath(dirPath));
        }

        /**
         * 删除目录
         *
         * @param dir 目录
         * @return {@code true}: 删除成功<br>{@code false}: 删除失败
         */
        public static boolean deleteDir(File dir) {
            if (dir == null) return false;
            // 目录不存在返回true
            if (!dir.exists()) return true;
            // 不是目录返回false
            if (!dir.isDirectory()) return false;
            // 现在文件存在且是文件夹
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
            return dir.delete();
        }

        /**
         * 删除文件
         *
         * @param srcFilePath 文件路径
         * @return {@code true}: 删除成功<br>{@code false}: 删除失败
         */
        public static boolean deleteFile(String srcFilePath) {
            return deleteFile(getFileByPath(srcFilePath));
        }

        /**
         * 删除文件
         *
         * @param file 文件
         * @return {@code true}: 删除成功<br>{@code false}: 删除失败
         */
        public static boolean deleteFile(File file) {
            return file != null && (!file.exists() || file.isFile() && file.delete());
        }

        /**
         * 获取目录下所有文件
         *
         * @param dirPath     目录路径
         * @param isRecursive 是否递归进子目录
         * @return 文件链表
         */
        public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
            return listFilesInDir(getFileByPath(dirPath), isRecursive);
        }

        /**
         * 获取目录下所有文件
         *
         * @param dir         目录
         * @param isRecursive 是否递归进子目录
         * @return 文件链表
         */
        public static List<File> listFilesInDir(File dir, boolean isRecursive) {
            if (isRecursive) return listFilesInDir(dir);
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            Collections.addAll(list, dir.listFiles());
            return list;
        }

        /**
         * 获取目录下所有文件包括子目录
         *
         * @param dirPath 目录路径
         * @return 文件链表
         */
        public static List<File> listFilesInDir(String dirPath) {
            return listFilesInDir(getFileByPath(dirPath));
        }

        /**
         * 获取目录下所有文件包括子目录
         *
         * @param dir 目录
         * @return 文件链表
         */
        public static List<File> listFilesInDir(File dir) {
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            File[] files = dir.listFiles();
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFilesInDir(file));
                }
            }
            return list;
        }

        /**
         * 获取目录下所有后缀名为suffix的文件
         * <p>大小写忽略</p>
         *
         * @param dirPath     目录路径
         * @param suffix      后缀名
         * @param isRecursive 是否递归进子目录
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(String dirPath, String suffix, boolean isRecursive) {
            return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive);
        }

        /**
         * 获取目录下所有后缀名为suffix的文件
         * <p>大小写忽略</p>
         *
         * @param dir         目录
         * @param suffix      后缀名
         * @param isRecursive 是否递归进子目录
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(File dir, String suffix, boolean isRecursive) {
            if (isRecursive) return listFilesInDirWithFilter(dir, suffix);
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
            }
            return list;
        }

        /**
         * 获取目录下所有后缀名为suffix的文件包括子目录
         * <p>大小写忽略</p>
         *
         * @param dirPath 目录路径
         * @param suffix  后缀名
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(String dirPath, String suffix) {
            return listFilesInDirWithFilter(getFileByPath(dirPath), suffix);
        }

        /**
         * 获取目录下所有后缀名为suffix的文件包括子目录
         * <p>大小写忽略</p>
         *
         * @param dir    目录
         * @param suffix 后缀名
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(File dir, String suffix) {
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, suffix));
                }
            }
            return list;
        }

        /**
         * 获取目录下所有符合filter的文件
         *
         * @param dirPath     目录路径
         * @param filter      过滤器
         * @param isRecursive 是否递归进子目录
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(String dirPath, FilenameFilter filter, boolean isRecursive) {
            return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
        }

        /**
         * 获取目录下所有符合filter的文件
         *
         * @param dir         目录
         * @param filter      过滤器
         * @param isRecursive 是否递归进子目录
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(File dir, FilenameFilter filter, boolean isRecursive) {
            if (isRecursive) return listFilesInDirWithFilter(dir, filter);
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            File[] files = dir.listFiles();
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
            }
            return list;
        }

        /**
         * 获取目录下所有符合filter的文件包括子目录
         *
         * @param dirPath 目录路径
         * @param filter  过滤器
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(String dirPath, FilenameFilter filter) {
            return listFilesInDirWithFilter(getFileByPath(dirPath), filter);
        }

        /**
         * 获取目录下所有符合filter的文件包括子目录
         *
         * @param dir    目录
         * @param filter 过滤器
         * @return 文件链表
         */
        public static List<File> listFilesInDirWithFilter(File dir, FilenameFilter filter) {
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            File[] files = dir.listFiles();
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, filter));
                }
            }
            return list;
        }

        /**
         * 获取目录下指定文件名的文件包括子目录
         * <p>大小写忽略</p>
         *
         * @param dirPath  目录路径
         * @param fileName 文件名
         * @return 文件链表
         */
        public static List<File> searchFileInDir(String dirPath, String fileName) {
            return searchFileInDir(getFileByPath(dirPath), fileName);
        }

        /**
         * 获取目录下指定文件名的文件包括子目录
         * <p>大小写忽略</p>
         *
         * @param dir      目录
         * @param fileName 文件名
         * @return 文件链表
         */
        public static List<File> searchFileInDir(File dir, String fileName) {
            if (dir == null || !isDir(dir)) return null;
            List<File> list = new ArrayList<>();
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().toUpperCase().equals(fileName.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, fileName));
                }
            }
            return list;
        }

        /**
         * 将输入流写入文件
         *
         * @param filePath 路径
         * @param is       输入流
         * @param append   是否追加在文件末
         * @return {@code true}: 写入成功<br>{@code false}: 写入失败
         */
        public static boolean writeFileFromIS(String filePath, InputStream is, boolean append) {
            return writeFileFromIS(getFileByPath(filePath), is, append);
        }

        /**
         * 将输入流写入文件
         *
         * @param file   文件
         * @param is     输入流
         * @param append 是否追加在文件末
         * @return {@code true}: 写入成功<br>{@code false}: 写入失败
         */
        public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
            if (file == null || is == null) return false;
            if (!createOrExistsFile(file)) return false;
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(file, append));
                byte data[] = new byte[KB];
                int len;
                while ((len = is.read(data, 0, KB)) != -1) {
                    os.write(data, 0, len);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                closeIO(is, os);
            }
        }

        /**
         * 将字符串写入文件
         *
         * @param filePath 文件路径
         * @param content  写入内容
         * @param append   是否追加在文件末
         * @return {@code true}: 写入成功<br>{@code false}: 写入失败
         */
        public static boolean writeFileFromString(String filePath, String content, boolean append) {
            return writeFileFromString(getFileByPath(filePath), content, append);
        }

        /**
         * 将字符串写入文件
         *
         * @param file    文件
         * @param content 写入内容
         * @param append  是否追加在文件末
         * @return {@code true}: 写入成功<br>{@code false}: 写入失败
         */
        public static boolean writeFileFromString(File file, String content, boolean append) {
            if (file == null || content == null) return false;
            if (!createOrExistsFile(file)) return false;
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file, append);
                fileWriter.write(content);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                closeIO(fileWriter);
            }
        }

        /**
         * 简单获取文件编码格式
         *
         * @param filePath 文件路径
         * @return 文件编码
         */
        public static String getFileCharsetSimple(String filePath) {
            return getFileCharsetSimple(getFileByPath(filePath));
        }

        /**
         * 简单获取文件编码格式
         *
         * @param file 文件
         * @return 文件编码
         */
        public static String getFileCharsetSimple(File file) {
            int p = 0;
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                p = (is.read() << 8) + is.read();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeIO(is);
            }
            switch (p) {
                case 0xefbb:
                    return "UTF-8";
                case 0xfffe:
                    return "Unicode";
                case 0xfeff:
                    return "UTF-16BE";
                default:
                    return "GBK";
            }
        }

        /**
         * 获取文件行数
         *
         * @param filePath 文件路径
         * @return 文件行数
         */
        public static int getFileLines(String filePath) {
            return getFileLines(getFileByPath(filePath));
        }

        /**
         * 获取文件行数
         *
         * @param file 文件
         * @return 文件行数
         */
        public static int getFileLines(File file) {
            int count = 1;
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[KB];
                int readChars;
                while ((readChars = is.read(buffer, 0, KB)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\n') ++count;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeIO(is);
            }
            return count;
        }

        /**
         * 指定编码按行读取文件到List
         *
         * @param filePath    文件路径
         * @param charsetName 编码格式
         * @return 文件行链表
         */
        public static List<String> readFile2List(String filePath, String charsetName) {
            return readFile2List(getFileByPath(filePath), charsetName);
        }

        /**
         * 指定编码按行读取文件到List
         *
         * @param file        文件
         * @param charsetName 编码格式
         * @return 文件行链表
         */
        public static List<String> readFile2List(File file, String charsetName) {
            return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
        }

        /**
         * 指定编码按行读取文件到List
         *
         * @param filePath    文件路径
         * @param st          需要读取的开始行数
         * @param end         需要读取的结束行数
         * @param charsetName 编码格式
         * @return 包含制定行的list
         */
        public static List<String> readFile2List(String filePath, int st, int end, String
                charsetName) {
            return readFile2List(getFileByPath(filePath), st, end, charsetName);
        }

        /**
         * 指定编码按行读取文件到List
         *
         * @param file        文件
         * @param st          需要读取的开始行数
         * @param end         需要读取的结束行数
         * @param charsetName 编码格式
         * @return 包含从start行到end行的list
         */
        public static List<String> readFile2List(File file, int st, int end, String charsetName) {
            if (file == null) return null;
            if (st > end) return null;
            BufferedReader reader = null;
            try {
                String line;
                int curLine = 1;
                List<String> list = new ArrayList<>();
                if (StringUtils.isSpace(charsetName)) {
                    reader = new BufferedReader(new FileReader(file));
                } else {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
                }
                while ((line = reader.readLine()) != null) {
                    if (curLine > end) break;
                    if (st <= curLine && curLine <= end) list.add(line);
                    ++curLine;
                }
                return list;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                closeIO(reader);
            }
        }

        /**
         * 指定编码按行读取文件到字符串中
         *
         * @param filePath    文件路径
         * @param charsetName 编码格式
         * @return 字符串
         */
        public static String readFile2String(String filePath, String charsetName) {
            return readFile2String(getFileByPath(filePath), charsetName);
        }

        /**
         * 指定编码按行读取文件到字符串中
         *
         * @param file        文件
         * @param charsetName 编码格式
         * @return 字符串
         */
        public static String readFile2String(File file, String charsetName) {
            if (file == null) return null;
            BufferedReader reader = null;
            try {
                StringBuilder sb = new StringBuilder();
                if (StringUtils.isSpace(charsetName)) {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                } else {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
                }
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\r\n");// windows系统换行为\r\n，Linux为\n
                }
                // 要去除最后的换行符
                return sb.delete(sb.length() - 2, sb.length()).toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                closeIO(reader);
            }
        }

        /**
         * 指定编码按行读取文件到字符串中
         *
         * @param filePath 文件路径
         * @return StringBuilder对象
         */
        public static byte[] readFile2Bytes(String filePath) {
            return readFile2Bytes(getFileByPath(filePath));
        }

        /**
         * 指定编码按行读取文件到字符串中
         *
         * @param file 文件
         * @return StringBuilder对象
         */
        public static byte[] readFile2Bytes(File file) {
            if (file == null) return null;
            try {
                return com.blankj.utilcode.utils.ConvertUtils.inputStream2Bytes(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 获取文件大小
         *
         * @param filePath 文件路径
         * @return 文件大小
         */
        public static String getFileSize(String filePath) {
            return getFileSize(getFileByPath(filePath));
        }

        /**
         * 获取文件大小
         * <p>例如：getFileSize(file, ConstUtils.MB); 返回文件大小单位为MB</p>
         *
         * @param file 文件
         * @return 文件大小
         */
        public static String getFileSize(File file) {
            if (!isFileExists(file)) return "";
            return com.blankj.utilcode.utils.ConvertUtils.byte2FitSize(file.length());
        }

        /**
         * 获取文件的MD5校验码
         *
         * @param filePath 文件
         * @return 文件的MD5校验码
         */
        public static String getFileMD5(String filePath) {
            return getFileMD5(getFileByPath(filePath));
        }

        /**
         * 获取文件的MD5校验码
         *
         * @param file 文件
         * @return 文件的MD5校验码
         */
        public static String getFileMD5(File file) {
            return com.blankj.utilcode.utils.EncryptUtils.encryptMD5File2String(file);
        }

        /**
         * 关闭IO
         *
         * @param closeables closeable
         */
        public static void closeIO(Closeable... closeables) {
            if (closeables == null) return;
            try {
                for (Closeable closeable : closeables) {
                    if (closeable != null) {
                        closeable.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取全路径中的最长目录
         *
         * @param file 文件
         * @return filePath最长目录
         */
        public static String getDirName(File file) {
            if (file == null) return null;
            return getDirName(file.getPath());
        }

        /**
         * 获取全路径中的最长目录
         *
         * @param filePath 文件路径
         * @return filePath最长目录
         */
        public static String getDirName(String filePath) {
            if (StringUtils.isSpace(filePath)) return filePath;
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
        }

        /**
         * 获取全路径中的文件名
         *
         * @param file 文件
         * @return 文件名
         */
        public static String getFileName(File file) {
            if (file == null) return null;
            return getFileName(file.getPath());
        }

        /**
         * 获取全路径中的文件名
         *
         * @param filePath 文件路径
         * @return 文件名
         */
        public static String getFileName(String filePath) {
            if (StringUtils.isSpace(filePath)) return filePath;
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
        }

        /**
         * 获取全路径中的不带拓展名的文件名
         *
         * @param file 文件
         * @return 不带拓展名的文件名
         */
        public static String getFileNameNoExtension(File file) {
            if (file == null) return null;
            return getFileNameNoExtension(file.getPath());
        }

        /**
         * 获取全路径中的不带拓展名的文件名
         *
         * @param filePath 文件路径
         * @return 不带拓展名的文件名
         */
        public static String getFileNameNoExtension(String filePath) {
            if (StringUtils.isSpace(filePath)) return filePath;
            int lastPoi = filePath.lastIndexOf('.');
            int lastSep = filePath.lastIndexOf(File.separator);
            if (lastSep == -1) {
                return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
            }
            if (lastPoi == -1 || lastSep > lastPoi) {
                return filePath.substring(lastSep + 1);
            }
            return filePath.substring(lastSep + 1, lastPoi);
        }

        /**
         * 获取全路径中的文件拓展名
         *
         * @param file 文件
         * @return 文件拓展名
         */
        public static String getFileExtension(File file) {
            if (file == null) return null;
            return getFileExtension(file.getPath());
        }

        /**
         * 获取全路径中的文件拓展名
         *
         * @param filePath 文件路径
         * @return 文件拓展名
         */
        public static String getFileExtension(String filePath) {
            if (StringUtils.isSpace(filePath)) return filePath;
            int lastPoi = filePath.lastIndexOf('.');
            int lastSep = filePath.lastIndexOf(File.separator);
            if (lastPoi == -1 || lastSep >= lastPoi) return "";
            return filePath.substring(lastPoi);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 键盘相关工具类
     * </pre>
     */
    public static class KeyboardUtils {

        private KeyboardUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 避免输入法面板遮挡
         * <p>在manifest.xml中activity中设置</p>
         * <p>android:windowSoftInputMode="stateVisible|adjustResize"</p>
         */

        /**
         * 动态隐藏软键盘
         *
         * @param activity activity
         */
        public static void hideSoftInput(Activity activity) {
            View view = activity.getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        /**
         * 动态隐藏软键盘
         *
         * @param context 上下文
         * @param edit    输入框
         */
        public static void hideSoftInput(Context context, EditText edit) {
            edit.clearFocus();
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }

        /**
         * 点击屏幕空白区域隐藏软键盘（方法1）
         * <p>在onTouch中处理，未获焦点则隐藏</p>
         * <p>参照以下注释代码</p>
         */
        public static void clickBlankArea2HideSoftInput0() {
            Log.i("tips", "U should copy the following code.");
        /*
        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (null != this.getCurrentFocus()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
            return super.onTouchEvent(event);
        }
        */
        }

        /**
         * 点击屏幕空白区域隐藏软键盘（方法2）
         * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
         * <p>需重写dispatchTouchEvent</p>
         * <p>参照以下注释代码</p>
         */
        public static void clickBlankArea2HideSoftInput1() {
            Log.i("tips", "U should copy the following code.");
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }

        // 获取InputMethodManager，隐藏软键盘
        private void hideKeyboard(IBinder token) {
            if (token != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        */
        }

        /**
         * 动态显示软键盘
         *
         * @param context 上下文
         * @param edit    输入框
         */
        public static void showSoftInput(Context context, EditText edit) {
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
            edit.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edit, 0);
        }

        /**
         * 切换键盘显示与否状态
         *
         * @param context 上下文
         * @param edit    输入框
         */
        public static void toggleSoftInput(Context context, EditText edit) {
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
            edit.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 网络相关工具类
     * </pre>
     */
    public static class NetworkUtils {

        private NetworkUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        public static final int NETWORK_WIFI = 1;    // wifi network
        public static final int NETWORK_4G = 4;    // "4G" networks
        public static final int NETWORK_3G = 3;    // "3G" networks
        public static final int NETWORK_2G = 2;    // "2G" networks
        public static final int NETWORK_UNKNOWN = 5;    // unknown network
        public static final int NETWORK_NO = -1;   // no network

        private static final int NETWORK_TYPE_GSM = 16;
        private static final int NETWORK_TYPE_TD_SCDMA = 17;
        private static final int NETWORK_TYPE_IWLAN = 18;

        /**
         * 打开网络设置界面
         * <p>3.0以下打开设置界面</p>
         *
         * @param context 上下文
         */
        public static void openWirelessSettings(Context context) {
            if (android.os.Build.VERSION.SDK_INT > 10) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            } else {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        }

        /**
         * 获取活动网络信息
         *
         * @param context 上下文
         * @return NetworkInfo
         */
        private static NetworkInfo getActiveNetworkInfo(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        }

        /**
         * 判断网络是否可用
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
         *
         * @param context 上下文
         * @return {@code true}: 可用<br>{@code false}: 不可用
         */
        public static boolean isAvailable(Context context) {
            NetworkInfo info = getActiveNetworkInfo(context);
            return info != null && info.isAvailable();
        }

        /**
         * 判断网络是否连接
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
         *
         * @param context 上下文
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isConnected(Context context) {
            NetworkInfo info = getActiveNetworkInfo(context);
            return info != null && info.isConnected();
        }

        /**
         * 判断网络是否是4G
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
         *
         * @param context 上下文
         * @return {@code true}: 是<br>{@code false}: 不是
         */
        public static boolean is4G(Context context) {
            NetworkInfo info = getActiveNetworkInfo(context);
            return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
        }

        /**
         * 判断wifi是否连接状态
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
         *
         * @param context 上下文
         * @return {@code true}: 连接<br>{@code false}: 未连接
         */
        public static boolean isWifiConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm != null && cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
        }

        /**
         * 获取移动网络运营商名称
         * <p>如中国联通、中国移动、中国电信</p>
         *
         * @param context 上下文
         * @return 移动网络运营商名称
         */
        public static String getNetworkOperatorName(Context context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm != null ? tm.getNetworkOperatorName() : null;
        }

        /**
         * 获取移动终端类型
         *
         * @param context 上下文
         * @return 手机制式
         * <ul>
         * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
         * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
         * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
         * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
         * </ul>
         */
        public static int getPhoneType(Context context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm != null ? tm.getPhoneType() : -1;
        }


        /**
         * 获取当前的网络类型(WIFI,2G,3G,4G)
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
         *
         * @param context 上下文
         * @return 网络类型
         * <ul>
         * <li>{@link #NETWORK_WIFI   } = 1;</li>
         * <li>{@link #NETWORK_4G     } = 4;</li>
         * <li>{@link #NETWORK_3G     } = 3;</li>
         * <li>{@link #NETWORK_2G     } = 2;</li>
         * <li>{@link #NETWORK_UNKNOWN} = 5;</li>
         * <li>{@link #NETWORK_NO     } = -1;</li>
         * </ul>
         */
        public static int getNetWorkType(Context context) {
            int netType = NETWORK_NO;
            NetworkInfo info = getActiveNetworkInfo(context);
            if (info != null && info.isAvailable()) {

                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    netType = NETWORK_WIFI;
                } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    switch (info.getSubtype()) {

                        case NETWORK_TYPE_GSM:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            netType = NETWORK_2G;
                            break;

                        case NETWORK_TYPE_TD_SCDMA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            netType = NETWORK_3G;
                            break;

                        case NETWORK_TYPE_IWLAN:
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            netType = NETWORK_4G;
                            break;
                        default:

                            String subtypeName = info.getSubtypeName();
                            if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                    || subtypeName.equalsIgnoreCase("WCDMA")
                                    || subtypeName.equalsIgnoreCase("CDMA2000")) {
                                netType = NETWORK_3G;
                            } else {
                                netType = NETWORK_UNKNOWN;
                            }
                            break;
                    }
                } else {
                    netType = NETWORK_UNKNOWN;
                }
            }
            return netType;
        }

        /**
         * 获取当前的网络类型(WIFI,2G,3G,4G)
         * <p>依赖上面的方法</p>
         *
         * @param context 上下文
         * @return 网络类型名称
         * <ul>
         * <li>NETWORK_WIFI   </li>
         * <li>NETWORK_4G     </li>
         * <li>NETWORK_3G     </li>
         * <li>NETWORK_2G     </li>
         * <li>NETWORK_UNKNOWN</li>
         * <li>NETWORK_NO     </li>
         * </ul>
         */
        public static String getNetWorkTypeName(Context context) {
            switch (getNetWorkType(context)) {
                case NETWORK_WIFI:
                    return "NETWORK_WIFI";
                case NETWORK_4G:
                    return "NETWORK_4G";
                case NETWORK_3G:
                    return "NETWORK_3G";
                case NETWORK_2G:
                    return "NETWORK_2G";
                case NETWORK_NO:
                    return "NETWORK_NO";
                default:
                    return "NETWORK_UNKNOWN";
            }
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 手机相关工具类
     * </pre>
     */
    public static class PhoneUtils {

        private PhoneUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 判断设备是否是手机
         *
         * @param context 上下文
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isPhone(Context context) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
        }

        /**
         * 获取手机的IMIE
         * <p>需与{@link #isPhone(Context)}一起使用</p>
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
         *
         * @param context 上下文
         * @return IMIE码
         */
        public static String getPhoneIMEI(Context context) {
            String deviceId;
            if (isPhone(context)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return deviceId;
        }

        /**
         * 获取手机状态信息
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
         *
         * @param context 上下文
         * @return DeviceId(IMEI) = 99000311726612<br>
         * DeviceSoftwareVersion = 00<br>
         * Line1Number =<br>
         * NetworkCountryIso = cn<br>
         * NetworkOperator = 46003<br>
         * NetworkOperatorName = 中国电信<br>
         * NetworkType = 6<br>
         * honeType = 2<br>
         * SimCountryIso = cn<br>
         * SimOperator = 46003<br>
         * SimOperatorName = 中国电信<br>
         * SimSerialNumber = 89860315045710604022<br>
         * SimState = 5<br>
         * SubscriberId(IMSI) = 460030419724900<br>
         * VoiceMailNumber = *86<br>
         */
        public static String getPhoneStatus(Context context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String str = "";
            str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
            str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
            str += "Line1Number = " + tm.getLine1Number() + "\n";
            str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
            str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
            str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
            str += "NetworkType = " + tm.getNetworkType() + "\n";
            str += "honeType = " + tm.getPhoneType() + "\n";
            str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
            str += "SimOperator = " + tm.getSimOperator() + "\n";
            str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
            str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
            str += "SimState = " + tm.getSimState() + "\n";
            str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
            str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
            return str;
        }

        /**
         * 跳至填充好phoneNumber的拨号界面
         *
         * @param context     上下文
         * @param phoneNumber 电话号码
         */
        public static void dial(Context context, String phoneNumber) {
            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
        }

        /**
         * 拨打phoneNumber
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
         *
         * @param context     上下文
         * @param phoneNumber 电话号码
         */
        public static void call(Context context, String phoneNumber) {
            context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber)));
        }

        /**
         * 发送短信
         *
         * @param context     上下文
         * @param phoneNumber 电话号码
         * @param content     内容
         */
        public static void sendSms(Context context, String phoneNumber, String content) {
            Uri uri = Uri.parse("smsto:" + (StringUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", StringUtils.isEmpty(content) ? "" : content);
            context.startActivity(intent);
        }

        /**
         * 获取手机联系人
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
         *
         * @param context 上下文;
         * @return 联系人链表
         */
        public static List<HashMap<String, String>> getAllContactInfo(Context context) {
            SystemClock.sleep(3000);
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            // 1.获取内容解析者
            ContentResolver resolver = context.getContentResolver();
            // 2.获取内容提供者的地址:com.android.contacts
            // raw_contacts表的地址 :raw_contacts
            // view_data表的地址 : data
            // 3.生成查询地址
            Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
            Uri date_uri = Uri.parse("content://com.android.contacts/data");
            // 4.查询操作,先查询raw_contacts,查询contact_id
            // projection : 查询的字段
            Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"},
                    null, null, null);
            // 5.解析cursor
            while (cursor.moveToNext()) {
                // 6.获取查询的数据
                String contact_id = cursor.getString(0);
                // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
                // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
                // 判断contact_id是否为空
                if (!StringUtils.isEmpty(contact_id)) {//null   ""
                    // 7.根据contact_id查询view_data表中的数据
                    // selection : 查询条件
                    // selectionArgs :查询条件的参数
                    // sortOrder : 排序
                    // 空指针: 1.null.方法 2.参数为null
                    Cursor c = resolver.query(date_uri, new String[]{"data1",
                                    "mimetype"}, "raw_contact_id=?",
                            new String[]{contact_id}, null);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // 8.解析c
                    while (c.moveToNext()) {
                        // 9.获取数据
                        String data1 = c.getString(0);
                        String mimetype = c.getString(1);
                        // 10.根据类型去判断获取的data1数据并保存
                        if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            // 电话
                            map.put("phone", data1);
                        } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                            // 姓名
                            map.put("name", data1);
                        }
                    }
                    // 11.添加到集合中数据
                    list.add(map);
                    // 12.关闭cursor
                    c.close();
                }
            }
            // 12.关闭cursor
            cursor.close();
            return list;
        }

        /**
         * 打开手机联系人界面点击联系人后便获取该号码
         * <p>参照以下注释代码</p>
         */
        public static void getContantNum() {
            Log.i("tips", "U should copy the following code.");
        /*
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 0);

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                Uri uri = data.getData();
                String num = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(uri,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    num = cursor.getString(cursor.getColumnIndex("data1"));
                }
                cursor.close();
                num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
            }
        }
        */
        }

        /**
         * 获取手机短信并保存到xml中
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_SMS"/>}</p>
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</p>
         *
         * @param context 上下文
         */
        public static void getAllSMS(Context context) {
            // 1.获取短信
            // 1.1获取内容解析者
            ContentResolver resolver = context.getContentResolver();
            // 1.2获取内容提供者地址   sms,sms表的地址:null  不写
            // 1.3获取查询路径
            Uri uri = Uri.parse("content://sms");
            // 1.4.查询操作
            // projection : 查询的字段
            // selection : 查询的条件
            // selectionArgs : 查询条件的参数
            // sortOrder : 排序
            Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
            // 设置最大进度
            int count = cursor.getCount();//获取短信的个数
            // 2.备份短信
            // 2.1获取xml序列器
            XmlSerializer xmlSerializer = Xml.newSerializer();
            try {
                // 2.2设置xml文件保存的路径
                // os : 保存的位置
                // encoding : 编码格式
                xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
                // 2.3设置头信息
                // standalone : 是否独立保存
                xmlSerializer.startDocument("utf-8", true);
                // 2.4设置根标签
                xmlSerializer.startTag(null, "smss");
                // 1.5.解析cursor
                while (cursor.moveToNext()) {
                    SystemClock.sleep(1000);
                    // 2.5设置短信的标签
                    xmlSerializer.startTag(null, "sms");
                    // 2.6设置文本内容的标签
                    xmlSerializer.startTag(null, "address");
                    String address = cursor.getString(0);
                    // 2.7设置文本内容
                    xmlSerializer.text(address);
                    xmlSerializer.endTag(null, "address");
                    xmlSerializer.startTag(null, "date");
                    String date = cursor.getString(1);
                    xmlSerializer.text(date);
                    xmlSerializer.endTag(null, "date");
                    xmlSerializer.startTag(null, "type");
                    String type = cursor.getString(2);
                    xmlSerializer.text(type);
                    xmlSerializer.endTag(null, "type");
                    xmlSerializer.startTag(null, "body");
                    String body = cursor.getString(3);
                    xmlSerializer.text(body);
                    xmlSerializer.endTag(null, "body");
                    xmlSerializer.endTag(null, "sms");
                    System.out.println("address:" + address + "   date:" + date + "  type:" + type + "  body:" + body);
                }
                xmlSerializer.endTag(null, "smss");
                xmlSerializer.endDocument();
                // 2.8将数据刷新到文件中
                xmlSerializer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 正则相关工具类
     * </pre>
     */
    public static class RegexUtils {

        private RegexUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * If u want more please visit http://toutiao.com/i6231678548520731137/
         */

        /**
         * 验证手机号（简单）
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isMobileSimple(String string) {
            return isMatch(REGEX_MOBILE_SIMPLE, string);
        }

        /**
         * 验证手机号（精确）
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isMobileExact(String string) {
            return isMatch(REGEX_MOBILE_EXACT, string);
        }

        /**
         * 验证电话号码
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isTel(String string) {
            return isMatch(REGEX_TEL, string);
        }

        /**
         * 验证身份证号码15位
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isIDCard15(String string) {
            return isMatch(REGEX_IDCARD15, string);
        }

        /**
         * 验证身份证号码18位
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isIDCard18(String string) {
            return isMatch(REGEX_IDCARD18, string);
        }

        /**
         * 验证邮箱
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isEmail(String string) {
            return isMatch(REGEX_EMAIL, string);
        }

        /**
         * 验证URL
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isURL(String string) {
            return isMatch(REGEX_URL, string);
        }

        /**
         * 验证汉字
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isChz(String string) {
            return isMatch(REGEX_CHZ, string);
        }

        /**
         * 验证用户名
         * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isUsername(String string) {
            return isMatch(REGEX_USERNAME, string);
        }

        /**
         * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isDate(String string) {
            return isMatch(REGEX_DATE, string);
        }

        /**
         * 验证IP地址
         *
         * @param string 待验证文本
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isIP(String string) {
            return isMatch(REGEX_IP, string);
        }

        /**
         * string是否匹配regex
         *
         * @param regex  正则表达式字符串
         * @param string 要匹配的字符串
         * @return {@code true}: 匹配<br>{@code false}: 不匹配
         */
        public static boolean isMatch(String regex, String string) {
            return !StringUtils.isEmpty(string) && Pattern.matches(regex, string);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 屏幕相关工具类
     * </pre>
     */
    public static class ScreenUtils {

        private ScreenUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 获取屏幕的宽度px
         *
         * @param context 上下文
         * @return 屏幕宽px
         */
        public static int getScreenWidth(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
            windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
            return outMetrics.widthPixels;
        }

        /**
         * 获取屏幕的高度px
         *
         * @param context 上下文
         * @return 屏幕高px
         */
        public static int getScreenHeight(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
            windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
            return outMetrics.heightPixels;
        }

        /**
         * 设置透明状态栏(api大于19方可使用)
         * <p>可在Activity的onCreat()中调用</p>
         * <p>需在顶部控件布局中加入以下属性让内容出现在状态栏之下</p>
         * <p>android:clipToPadding="true"</p>
         * <p>android:fitsSystemWindows="true"</p>
         *
         * @param activity activity
         */
        public static void setTransparentStatusBar(Activity activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //透明状态栏
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //透明导航栏
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

        /**
         * 隐藏状态栏
         * <p>也就是设置全屏，一定要在setContentView之前调用，否则报错</p>
         * <p>此方法Activity可以继承AppCompatActivity</p>
         * <p>启动的时候状态栏会显示一下再隐藏，比如QQ的欢迎界面</p>
         * <p>在配置文件中Activity加属性android:theme="@android:style/Theme.NoTitleBar.Fullscreen"</p>
         * <p>如加了以上配置Activity不能继承AppCompatActivity，会报错</p>
         *
         * @param activity activity
         */
        public static void hideStatusBar(Activity activity) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        /**
         * 获取状态栏高度
         *
         * @param context 上下文
         * @return 状态栏高度
         */
        public static int getStatusBarHeight(Context context) {
            int result = 0;
            int resourceId = context.getResources()
                    .getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        /**
         * 判断状态栏是否存在
         *
         * @param activity activity
         * @return {@code true}: 存在<br>{@code false}: 不存在
         */
        public static boolean isStatusBarExists(Activity activity) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            return (params.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }

        /**
         * 获取ActionBar高度
         *
         * @param activity activity
         * @return ActionBar高度
         */
        public static int getActionBarHeight(Activity activity) {
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            }
            return 0;
        }

        /**
         * 显示通知栏
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>}</p>
         *
         * @param context        上下文
         * @param isSettingPanel {@code true}: 打开设置<br>{@code false}: 打开通知
         */
        public static void showNotificationBar(Context context, boolean isSettingPanel) {
            String methodName = (Build.VERSION.SDK_INT <= 16) ? "expand"
                    : (isSettingPanel ? "expandSettingsPanel" : "expandNotificationsPanel");
            invokePanels(context, methodName);
        }

        /**
         * 隐藏通知栏
         * <p>需添加权限 {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>}</p>
         *
         * @param context 上下文
         */
        public static void hideNotificationBar(Context context) {
            String methodName = (Build.VERSION.SDK_INT <= 16) ? "collapse" : "collapsePanels";
            invokePanels(context, methodName);
        }

        /**
         * 反射唤醒通知栏
         *
         * @param context    上下文
         * @param methodName 方法名
         */
        private static void invokePanels(Context context, String methodName) {
            try {
                Object service = context.getSystemService("statusbar");
                Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
                Method expand = statusBarManager.getMethod(methodName);
                expand.invoke(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 设置屏幕为横屏
         * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
         * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
         * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
         * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
         * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
         *
         * @param activity activity
         */
        public static void setLandscape(Activity activity) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        /**
         * 获取当前屏幕截图，包含状态栏
         *
         * @param activity activity
         * @return Bitmap
         */
        public static Bitmap captureWithStatusBar(Activity activity) {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bmp = view.getDrawingCache();
            int width = getScreenWidth(activity);
            int height = getScreenHeight(activity);
            Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
            view.destroyDrawingCache();
            return bp;
        }

        /**
         * 获取当前屏幕截图，不包含状态栏
         * <p>需要用到上面获取状态栏高度getStatusBarHeight的方法</p>
         *
         * @param activity activity
         * @return Bitmap
         */
        public static Bitmap captureWithoutStatusBar(Activity activity) {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bmp = view.getDrawingCache();
            int statusBarHeight = getStatusBarHeight(activity);
            int width = getScreenWidth(activity);
            int height = getScreenHeight(activity);
            Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
            view.destroyDrawingCache();
            return bp;
        }

        /**
         * 判断是否锁屏
         *
         * @param context 上下文
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isScreenLock(Context context) {
            KeyguardManager km = (KeyguardManager) context
                    .getSystemService(Context.KEYGUARD_SERVICE);
            return km.inKeyguardRestrictedInputMode();
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/11
     *     desc  : SD卡相关工具类
     * </pre>
     */
    public static class SDCardUtils {

        private SDCardUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 判断SD卡是否可用
         *
         * @return true : 可用<br>false : 不可用
         */
        public static boolean isSDCardEnable() {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }

        /**
         * 获取SD卡路径
         * <p>一般是/storage/emulated/0/</p>
         *
         * @return SD卡路径
         */
        public static String getSDCardPath() {
            return Environment.getExternalStorageDirectory().getPath() + File.separator;
        }

        /**
         * 获取SD卡Data路径
         *
         * @return Data路径
         */
        public static String getDataPath() {
            return Environment.getDataDirectory().getPath();

        }

        /**
         * 计算SD卡的剩余空间
         *
         * @param unit <ul>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#BYTE}: 字节</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#KB}  : 千字节</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#MB}  : 兆</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.MemoryUnit#GB}  : GB</li>
         *             </ul>
         * @return 返回-1，说明SD卡不可用，否则返回SD卡剩余空间
         */
        public static String getFreeSpace(com.blankj.utilcode.utils.ConstUtils.MemoryUnit unit) {
            if (!isSDCardEnable()) return "sdcard unable!";
            try {
                StatFs stat = new StatFs(getSDCardPath());
                long blockSize, availableBlocks;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    availableBlocks = stat.getAvailableBlocksLong();
                    blockSize = stat.getBlockSizeLong();
                } else {
                    availableBlocks = stat.getAvailableBlocks();
                    blockSize = stat.getBlockSize();
                }
                return com.blankj.utilcode.utils.ConvertUtils.byte2FitSize(availableBlocks * blockSize);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/7
     *     desc  : Shell相关工具类
     * </pre>
     */
    public static class ShellUtils {

        private ShellUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        public static final String COMMAND_SU = "su";
        public static final String COMMAND_SH = "sh";
        public static final String COMMAND_EXIT = "exit\n";
        public static final String COMMAND_LINE_END = "\n";

        /**
         * 判断设备是否root
         * @return {@code true}: root<br>{@code false}: 没root
         */
        public static boolean isRoot() {
            return execCmd("echo root", true, false).result == 0;
        }

        /**
         * 是否是在root下执行命令
         *
         * @param command 命令
         * @param isRoot  是否root
         * @return CommandResult
         */
        public static com.blankj.utilcode.utils.ShellUtils.CommandResult execCmd(String command, boolean isRoot) {
            return execCmd(new String[]{command}, isRoot, true);
        }

        /**
         * 是否是在root下执行命令
         *
         * @param commands 多条命令链表
         * @param isRoot   是否root
         * @return CommandResult
         */
        public static com.blankj.utilcode.utils.ShellUtils.CommandResult execCmd(List<String> commands, boolean isRoot) {
            return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
        }

        /**
         * 是否是在root下执行命令
         *
         * @param commands 多条命令数组
         * @param isRoot   是否root
         * @return CommandResult
         */
        public static com.blankj.utilcode.utils.ShellUtils.CommandResult execCmd(String[] commands, boolean isRoot) {
            return execCmd(commands, isRoot, true);
        }

        /**
         * 是否是在root下执行命令
         *
         * @param command         命令
         * @param isRoot          是否root
         * @param isNeedResultMsg 是否需要结果消息
         * @return CommandResult
         */
        public static com.blankj.utilcode.utils.ShellUtils.CommandResult execCmd(String command, boolean isRoot, boolean isNeedResultMsg) {
            return execCmd(new String[]{command}, isRoot, isNeedResultMsg);
        }

        /**
         * 是否是在root下执行命令
         *
         * @param commands        命令链表
         * @param isRoot          是否root
         * @param isNeedResultMsg 是否需要结果消息
         * @return CommandResult
         */
        public static com.blankj.utilcode.utils.ShellUtils.CommandResult execCmd(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
            return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
        }

        /**
         * 是否是在root下执行命令
         *
         * @param commands        命令数组
         * @param isRoot          是否root
         * @param isNeedResultMsg 是否需要结果消息
         * @return CommandResult
         */
        public static com.blankj.utilcode.utils.ShellUtils.CommandResult execCmd(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
            int result = -1;
            if (commands == null || commands.length == 0) {
                return new com.blankj.utilcode.utils.ShellUtils.CommandResult(result, null, null);
            }
            Process process = null;
            BufferedReader successResult = null;
            BufferedReader errorResult = null;
            StringBuilder successMsg = null;
            StringBuilder errorMsg = null;
            DataOutputStream os = null;
            try {
                process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
                os = new DataOutputStream(process.getOutputStream());
                for (String command : commands) {
                    if (command == null) {
                        continue;
                    }
                    os.write(command.getBytes());
                    os.writeBytes(COMMAND_LINE_END);
                    os.flush();
                }
                os.writeBytes(COMMAND_EXIT);
                os.flush();

                result = process.waitFor();
                if (isNeedResultMsg) {
                    successMsg = new StringBuilder();
                    errorMsg = new StringBuilder();
                    successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String s;
                    while ((s = successResult.readLine()) != null) {
                        successMsg.append(s);
                    }
                    while ((s = errorResult.readLine()) != null) {
                        errorMsg.append(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (successResult != null) {
                        successResult.close();
                    }
                    if (errorResult != null) {
                        errorResult.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (process != null) {
                    process.destroy();
                }
            }
            return new com.blankj.utilcode.utils.ShellUtils.CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                    : errorMsg.toString());
        }

        /**
         * 返回的命令结果
         */
        public static class CommandResult {

            /**
             * 结果码
             **/
            public int result;
            /**
             * 成功的信息
             **/
            public String successMsg;
            /**
             * 错误信息
             **/
            public String errorMsg;

            public CommandResult(int result) {
                this.result = result;
            }

            public CommandResult(int result, String successMsg, String errorMsg) {
                this.result = result;
                this.successMsg = successMsg;
                this.errorMsg = errorMsg;
            }
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 尺寸相关工具类
     * </pre>
     */
    public static class SizeUtils {

        private SizeUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * dp转px
         *
         * @param context 上下文
         * @param dpValue dp值
         * @return px值
         */
        public static int dp2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        /**
         * px转dp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return dp值
         */
        public static int px2dp(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        /**
         * sp转px
         *
         * @param context 上下文
         * @param spValue sp值
         * @return px值
         */
        public static int sp2px(Context context, float spValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        }

        /**
         * px转sp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return sp值
         */
        public static int px2sp(Context context, float pxValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (pxValue / fontScale + 0.5f);
        }

        /**
         * 各种单位转换
         * <p>该方法存在于TypedValue</p>
         *
         * @param unit    单位
         * @param value   值
         * @param metrics DisplayMetrics
         * @return 转换结果
         */
        public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
            switch (unit) {
                case TypedValue.COMPLEX_UNIT_PX:
                    return value;
                case TypedValue.COMPLEX_UNIT_DIP:
                    return value * metrics.density;
                case TypedValue.COMPLEX_UNIT_SP:
                    return value * metrics.scaledDensity;
                case TypedValue.COMPLEX_UNIT_PT:
                    return value * metrics.xdpi * (1.0f / 72);
                case TypedValue.COMPLEX_UNIT_IN:
                    return value * metrics.xdpi;
                case TypedValue.COMPLEX_UNIT_MM:
                    return value * metrics.xdpi * (1.0f / 25.4f);
            }
            return 0;
        }

        /**
         * 在onCreate()即可强行获取View的尺寸
         * <p>需回调onGetSizeListener接口，在onGetSize中获取view宽高</p>
         * <p>用法示例如下所示</p>
         * <pre>{@code
         * SizeUtils.forceGetViewSize(view);
         * SizeUtils.setListener(new SizeUtils.onGetSizeListener() {
         *     public void onGetSize(View view) {
         *         Log.d("tag", view.getWidth() + " " + view.getHeight());
         *     }
         * });}
         * </pre>
         *
         * @param view 视图
         */
        public static void forceGetViewSize(final View view) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onGetSize(view);
                    }
                }
            });
        }

        /**
         * 获取到View尺寸的监听
         */
        public interface onGetSizeListener {
            void onGetSize(View view);
        }

        public static void setListener(com.blankj.utilcode.utils.SizeUtils.onGetSizeListener listener) {
            mListener = listener;
        }

        private static com.blankj.utilcode.utils.SizeUtils.onGetSizeListener mListener;

        /**
         * ListView中提前测量View尺寸，如headerView
         * <p>用的时候去掉注释拷贝到ListView中即可</p>
         * <p>参照以下注释代码</p>
         *
         * @param view 视图
         */
        public static void measureViewInLV(View view) {
            Log.i("tips", "U should copy the following code.");
        /*
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
        */
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : SP相关工具类
     * </pre>
     */
    public class SPUtils {

        private SharedPreferences sp;
        private SharedPreferences.Editor editor;

        /**
         * SPUtils构造函数
         *
         * @param context 上下文
         * @param spName  spName
         */
        public SPUtils(Context context, String spName) {
            sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.apply();
        }

        /**
         * SP中写入String类型value
         *
         * @param key   键
         * @param value 值
         */
        public void putString(String key, String value) {
            editor.putString(key, value).apply();
        }

        /**
         * SP中读取String
         *
         * @param key 键
         * @return 存在返回对应值，不存在返回默认值{@code null}
         */
        public String getString(String key) {
            return getString(key, null);
        }

        /**
         * SP中读取String
         *
         * @param key          键
         * @param defaultValue 默认值
         * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
         */
        public String getString(String key, String defaultValue) {
            return sp.getString(key, defaultValue);
        }

        /**
         * SP中写入int类型value
         *
         * @param key   键
         * @param value 值
         */
        public void putInt(String key, int value) {
            editor.putInt(key, value).apply();
        }

        /**
         * SP中读取int
         *
         * @param key 键
         * @return 存在返回对应值，不存在返回默认值-1
         */
        public int getInt(String key) {
            return getInt(key, -1);
        }

        /**
         * SP中读取int
         *
         * @param key          键
         * @param defaultValue 默认值
         * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
         */
        public int getInt(String key, int defaultValue) {
            return sp.getInt(key, defaultValue);
        }

        /**
         * SP中写入long类型value
         *
         * @param key   键
         * @param value 值
         */
        public void putLong(String key, long value) {
            editor.putLong(key, value).apply();
        }

        /**
         * SP中读取long
         *
         * @param key 键
         * @return 存在返回对应值，不存在返回默认值-1
         */
        public long getLong(String key) {
            return getLong(key, -1L);
        }

        /**
         * SP中读取long
         *
         * @param key          键
         * @param defaultValue 默认值
         * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
         */
        public long getLong(String key, long defaultValue) {
            return sp.getLong(key, defaultValue);
        }

        /**
         * SP中写入float类型value
         *
         * @param key   键
         * @param value 值
         */
        public void putFloat(String key, float value) {
            editor.putFloat(key, value).apply();
        }

        /**
         * SP中读取float
         *
         * @param key 键
         * @return 存在返回对应值，不存在返回默认值-1
         */
        public float getFloat(String key) {
            return getFloat(key, -1f);
        }

        /**
         * SP中读取float
         *
         * @param key          键
         * @param defaultValue 默认值
         * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
         */
        public float getFloat(String key, float defaultValue) {
            return sp.getFloat(key, defaultValue);
        }

        /**
         * SP中写入boolean类型value
         *
         * @param key   键
         * @param value 值
         */
        public void putBoolean(String key, boolean value) {
            editor.putBoolean(key, value).apply();
        }

        /**
         * SP中读取boolean
         *
         * @param key 键
         * @return 存在返回对应值，不存在返回默认值{@code false}
         */
        public boolean getBoolean(String key) {
            return getBoolean(key, false);
        }

        /**
         * SP中读取boolean
         *
         * @param key          键
         * @param defaultValue 默认值
         * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
         */
        public boolean getBoolean(String key, boolean defaultValue) {
            return sp.getBoolean(key, defaultValue);
        }

        /**
         * SP中获取所有键值对
         *
         * @return Map对象
         */
        public Map<String, ?> getAll() {
            return sp.getAll();
        }

        /**
         * SP中移除该key
         *
         * @param key 键
         */
        public void remove(String key) {
            editor.remove(key).apply();
        }

        /**
         * SP中是否存在该key
         *
         * @param key 键
         * @return {@code true}: 存在<br>{@code false}: 不存在
         */
        public boolean contains(String key) {
            return sp.contains(key);
        }

        /**
         * SP中清除所有数据
         */
        public void clear() {
            editor.clear().apply();
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/16
     *     desc  : 字符串相关工具类
     * </pre>
     */
    public static class StringUtils {

        private StringUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 判断字符串是否为null或长度为0
         *
         * @param s 待校验字符串
         * @return {@code true}: 空<br> {@code false}: 不为空
         */
        public static boolean isEmpty(CharSequence s) {
            return s == null || s.length() == 0;
        }

        /**
         * 判断字符串是否为null或全为空格
         *
         * @param s 待校验字符串
         * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
         */
        public static boolean isSpace(String s) {
            return (s == null || s.trim().length() == 0);
        }

        /**
         * null转为长度为0的字符串
         *
         * @param s 待转字符串
         * @return s为null转为长度为0字符串，否则不改变
         */
        public static String null2Length0(String s) {
            return s == null ? "" : s;
        }

        /**
         * 返回字符串长度
         *
         * @param s 字符串
         * @return null返回0，其他返回自身长度
         */
        public static int length(CharSequence s) {
            return s == null ? 0 : s.length();
        }

        /**
         * 首字母大写
         *
         * @param s 待转字符串
         * @return 首字母大写字符串
         */
        public static String upperFirstLetter(String s) {
            if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
                return s;
            }
            return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
        }

        /**
         * 首字母小写
         *
         * @param s 待转字符串
         * @return 首字母小写字符串
         */
        public static String lowerFirstLetter(String s) {
            if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
                return s;
            }
            return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
        }

        /**
         * 反转字符串
         *
         * @param s 待反转字符串
         * @return 反转字符串
         */
        public static String reverse(String s) {
            int len = length(s);
            if (len <= 1) return s;
            int mid = len >> 1;
            char[] chars = s.toCharArray();
            char c;
            for (int i = 0; i < mid; ++i) {
                c = chars[i];
                chars[i] = chars[len - i - 1];
                chars[len - i - 1] = c;
            }
            return new String(chars);
        }

        /**
         * 转化为半角字符
         *
         * @param s 待转字符串
         * @return 半角字符串
         */
        public static String toDBC(String s) {
            if (isEmpty(s)) {
                return s;
            }
            char[] chars = s.toCharArray();
            for (int i = 0, len = chars.length; i < len; i++) {
                if (chars[i] == 12288) {
                    chars[i] = ' ';
                } else if (65281 <= chars[i] && chars[i] <= 65374) {
                    chars[i] = (char) (chars[i] - 65248);
                } else {
                    chars[i] = chars[i];
                }
            }
            return new String(chars);
        }

        /**
         * 转化为全角字符
         *
         * @param s 待转字符串
         * @return 全角字符串
         */
        public static String toSBC(String s) {
            if (isEmpty(s)) {
                return s;
            }
            char[] chars = s.toCharArray();
            for (int i = 0, len = chars.length; i < len; i++) {
                if (chars[i] == ' ') {
                    chars[i] = (char) 12288;
                } else if (33 <= chars[i] && chars[i] <= 126) {
                    chars[i] = (char) (chars[i] + 65248);
                } else {
                    chars[i] = chars[i];
                }
            }
            return new String(chars);
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/25
     *     desc  : 线程池相关工具类
     * </pre>
     */
    public static class ThreadPoolUtils {

        public enum Type {
            FixedThread,
            CachedThread,
            SingleThread,
        }

        private ExecutorService exec;
        private ScheduledExecutorService scheduleExec;

        /**
         * ThreadPoolUtils构造函数
         *
         * @param type         线程池类型
         * @param corePoolSize 只对Fixed和Scheduled线程池起效
         */
        public ThreadPoolUtils(com.blankj.utilcode.utils.ThreadPoolUtils.Type type, int corePoolSize) {
            // 构造有定时功能的线程池
            // ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 10L, TimeUnit.MILLISECONDS, new BlockingQueue<Runnable>)
            scheduleExec = Executors.newScheduledThreadPool(corePoolSize);
            switch (type) {
                case FixedThread:
                    // 构造一个固定线程数目的线程池
                    // ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new
                    // LinkedBlockingQueue<Runnable>());
                    exec = Executors.newFixedThreadPool(corePoolSize);
                    break;
                case SingleThread:
                    // 构造一个只支持一个线程的线程池,相当于newFixedThreadPool(1)
                    // ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
                    exec = Executors.newSingleThreadExecutor();
                    break;
                case CachedThread:
                    // 构造一个缓冲功能的线程池
                    // ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                    exec = Executors.newCachedThreadPool();
                    break;
                default:
                    exec = scheduleExec;
                    break;
            }
        }

        /**
         * 在未来某个时间执行给定的命令
         * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
         *
         * @param command 命令
         */
        public void execute(Runnable command) {
            exec.execute(command);
        }

        /**
         * 在未来某个时间执行给定的命令链表
         * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
         *
         * @param commands 命令链表
         */
        public void execute(List<Runnable> commands) {
            for (Runnable command : commands) {
                exec.execute(command);
            }
        }

        /**
         * 待以前提交的任务执行完毕后关闭线程池
         * <p>启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
         * 如果已经关闭，则调用没有作用。</p>
         */
        public void shutDown() {
            exec.shutdown();
        }

        /**
         * 试图停止所有正在执行的活动任务
         * <p>试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。</p>
         * <p>无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。</p>
         *
         * @return 等待执行的任务的列表
         */
        public List<Runnable> shutDownNow() {
            return exec.shutdownNow();
        }

        /**
         * 判断线程池是否已关闭
         *
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public boolean isShutDown() {
            return exec.isShutdown();
        }

        /**
         * 关闭线程池后判断所有任务是否都已完成
         * <p>注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。</p>
         *
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public boolean isTerminated() {
            return exec.isTerminated();
        }


        /**
         * 请求关闭、发生超时或者当前线程中断
         * <p>无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。</p>
         *
         * @param timeout 最长等待时间
         * @param unit    时间单位
         * @return {@code true}: 请求成功<br>{@code false}: 请求超时
         */
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return exec.awaitTermination(timeout, unit);
        }

        /**
         * 提交一个Callable任务用于执行
         * <p>如果想立即阻塞任务的等待，则可以使用{@code result = exec.submit(aCallable).get();}形式的构造。</p>
         *
         * @param task 任务
         * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
         */
        public <T> Future<T> submit(Callable<T> task) {
            return exec.submit(task);
        }

        /**
         * 提交一个Runnable任务用于执行
         *
         * @param task   任务
         * @param result 返回的结果
         * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
         */
        public <T> Future<T> submit(Runnable task, T result) {
            return exec.submit(task, result);
        }

        /**
         * 提交一个Runnable任务用于执行
         *
         * @param task 任务
         * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回null结果。
         */
        public Future<?> submit(Runnable task) {
            return exec.submit(task);
        }

        /**
         * 执行给定的任务
         * <p>当所有任务完成时，返回保持任务状态和结果的Future列表。
         * 返回列表的所有元素的{@link Future#isDone}为{@code true}。
         * 注意，可以正常地或通过抛出异常来终止已完成任务。
         * 如果正在进行此操作时修改了给定的 collection，则此方法的结果是不确定的。</p>
         *
         * @param tasks 任务集合
         * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同，每个任务都已完成。
         * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务。
         */
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return exec.invokeAll(tasks);
        }

        /**
         * 执行给定的任务
         * <p>当所有任务完成或超时期满时(无论哪个首先发生)，返回保持任务状态和结果的Future列表。
         * 返回列表的所有元素的{@link Future#isDone}为{@code true}。
         * 一旦返回后，即取消尚未完成的任务。
         * 注意，可以正常地或通过抛出异常来终止已完成任务。
         * 如果此操作正在进行时修改了给定的 collection，则此方法的结果是不确定的。</p>
         *
         * @param tasks   任务集合
         * @param timeout 最长等待时间
         * @param unit    时间单位
         * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同。如果操作未超时，则已完成所有任务。如果确实超时了，则某些任务尚未完成。
         * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
         */
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
                InterruptedException {
            return exec.invokeAll(tasks, timeout, unit);
        }

        /**
         * 执行给定的任务
         * <p>如果某个任务已成功完成（也就是未抛出异常），则返回其结果。
         * 一旦正常或异常返回后，则取消尚未完成的任务。
         * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。</p>
         *
         * @param tasks 任务集合
         * @return 某个任务返回的结果
         * @throws InterruptedException 如果等待时发生中断
         * @throws ExecutionException   如果没有任务成功完成
         */
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return exec.invokeAny(tasks);
        }

        /**
         * 执行给定的任务
         * <p>如果在给定的超时期满前某个任务已成功完成（也就是未抛出异常），则返回其结果。
         * 一旦正常或异常返回后，则取消尚未完成的任务。
         * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。</p>
         *
         * @param tasks   任务集合
         * @param timeout 最长等待时间
         * @param unit    时间单位
         * @return 某个任务返回的结果
         * @throws InterruptedException 如果等待时发生中断
         * @throws ExecutionException   如果没有任务成功完成
         * @throws TimeoutException     如果在所有任务成功完成之前给定的超时期满
         */
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
                InterruptedException, ExecutionException, TimeoutException {
            return exec.invokeAny(tasks, timeout, unit);
        }

        /**
         * 延迟执行Runnable命令
         *
         * @param command 命令
         * @param delay   延迟时间
         * @param unit    单位
         * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在完成后将返回{@code null}
         */
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            return scheduleExec.schedule(command, delay, unit);
        }

        /**
         * 延迟执行Callable命令
         *
         * @param callable 命令
         * @param delay    延迟时间
         * @param unit     时间单位
         * @return 可用于提取结果或取消的ScheduledFuture
         */
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            return scheduleExec.schedule(callable, delay, unit);
        }

        /**
         * 延迟并循环执行命令
         *
         * @param command      命令
         * @param initialDelay 首次执行的延迟时间
         * @param period       连续执行之间的周期
         * @param unit         时间单位
         * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在取消后将抛出异常
         */
        public ScheduledFuture<?> scheduleWithFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            return scheduleExec.scheduleAtFixedRate(command, initialDelay, period, unit);
        }

        /**
         * 延迟并以固定休息时间循环执行命令
         *
         * @param command      命令
         * @param initialDelay 首次执行的延迟时间
         * @param delay        每一次执行终止和下一次执行开始之间的延迟
         * @param unit         时间单位
         * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在取消后将抛出异常
         */
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            return scheduleExec.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        }
    }



    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 时间相关工具类
     * </pre>
     */
    public static class TimeUtils {

        private TimeUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * <p>在工具类中经常使用到工具类的格式化描述，这个主要是一个日期的操作类，所以日志格式主要使用 SimpleDateFormat的定义格式.</p>
         * 格式的意义如下： 日期和时间模式 <br>
         * <p>日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
         * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
         * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
         * </p>
         * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br>
         * <table border="1" cellspacing="1" cellpadding="1" summary="Chart shows pattern letters, date/time component,
         * presentation, and examples.">
         * <tr>
         * <th align="left">字母</th>
         * <th align="left">日期或时间元素</th>
         * <th align="left">表示</th>
         * <th align="left">示例</th>
         * </tr>
         * <tr>
         * <td><code>G</code></td>
         * <td>Era 标志符</td>
         * <td>Text</td>
         * <td><code>AD</code></td>
         * </tr>
         * <tr>
         * <td><code>y</code> </td>
         * <td>年 </td>
         * <td>Year </td>
         * <td><code>1996</code>; <code>96</code> </td>
         * </tr>
         * <tr>
         * <td><code>M</code> </td>
         * <td>年中的月份 </td>
         * <td>Month </td>
         * <td><code>July</code>; <code>Jul</code>; <code>07</code> </td>
         * </tr>
         * <tr>
         * <td><code>w</code> </td>
         * <td>年中的周数 </td>
         * <td>Number </td>
         * <td><code>27</code> </td>
         * </tr>
         * <tr>
         * <td><code>W</code> </td>
         * <td>月份中的周数 </td>
         * <td>Number </td>
         * <td><code>2</code> </td>
         * </tr>
         * <tr>
         * <td><code>D</code> </td>
         * <td>年中的天数 </td>
         * <td>Number </td>
         * <td><code>189</code> </td>
         * </tr>
         * <tr>
         * <td><code>d</code> </td>
         * <td>月份中的天数 </td>
         * <td>Number </td>
         * <td><code>10</code> </td>
         * </tr>
         * <tr>
         * <td><code>F</code> </td>
         * <td>月份中的星期 </td>
         * <td>Number </td>
         * <td><code>2</code> </td>
         * </tr>
         * <tr>
         * <td><code>E</code> </td>
         * <td>星期中的天数 </td>
         * <td>Text </td>
         * <td><code>Tuesday</code>; <code>Tue</code> </td>
         * </tr>
         * <tr>
         * <td><code>a</code> </td>
         * <td>Am/pm 标记 </td>
         * <td>Text </td>
         * <td><code>PM</code> </td>
         * </tr>
         * <tr>
         * <td><code>H</code> </td>
         * <td>一天中的小时数（0-23） </td>
         * <td>Number </td>
         * <td><code>0</code> </td>
         * </tr>
         * <tr>
         * <td><code>k</code> </td>
         * <td>一天中的小时数（1-24） </td>
         * <td>Number </td>
         * <td><code>24</code> </td>
         * </tr>
         * <tr>
         * <td><code>K</code> </td>
         * <td>am/pm 中的小时数（0-11） </td>
         * <td>Number </td>
         * <td><code>0</code> </td>
         * </tr>
         * <tr>
         * <td><code>h</code> </td>
         * <td>am/pm 中的小时数（1-12） </td>
         * <td>Number </td>
         * <td><code>12</code> </td>
         * </tr>
         * <tr>
         * <td><code>m</code> </td>
         * <td>小时中的分钟数 </td>
         * <td>Number </td>
         * <td><code>30</code> </td>
         * </tr>
         * <tr>
         * <td><code>s</code> </td>
         * <td>分钟中的秒数 </td>
         * <td>Number </td>
         * <td><code>55</code> </td>
         * </tr>
         * <tr>
         * <td><code>S</code> </td>
         * <td>毫秒数 </td>
         * <td>Number </td>
         * <td><code>978</code> </td>
         * </tr>
         * <tr>
         * <td><code>z</code> </td>
         * <td>时区 </td>
         * <td>General time zone </td>
         * <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code> </td>
         * </tr>
         * <tr>
         * <td><code>Z</code> </td>
         * <td>时区 </td>
         * <td>RFC 822 time zone </td>
         * <td><code>-0800</code> </td>
         * </tr>
         * </table>
         * <pre>
         *                          HH:mm    15:44
         *                         h:mm a    3:44 下午
         *                        HH:mm z    15:44 CST
         *                        HH:mm Z    15:44 +0800
         *                     HH:mm zzzz    15:44 中国标准时间
         *                       HH:mm:ss    15:44:40
         *                     yyyy-MM-dd    2016-08-12
         *               yyyy-MM-dd HH:mm    2016-08-12 15:44
         *            yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
         *       yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
         *  EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
         *       yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
         *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
         *   yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
         *                         K:mm a    3:44 下午
         *               EEE, MMM d, ''yy    星期五, 八月 12, '16
         *          hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
         *   yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
         *     EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
         *                  yyMMddHHmmssZ    160812154440+0800
         *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
         * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
         * </pre>
         */
        public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


        /**
         * 将时间戳转为时间字符串
         * <p>格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param milliseconds 毫秒时间戳
         * @return 时间字符串
         */
        public static String milliseconds2String(long milliseconds) {
            return milliseconds2String(milliseconds, DEFAULT_SDF);
        }

        /**
         * 将时间戳转为时间字符串
         * <p>格式为用户自定义</p>
         *
         * @param milliseconds 毫秒时间戳
         * @param format       时间格式
         * @return 时间字符串
         */
        public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
            return format.format(new Date(milliseconds));
        }

        /**
         * 将时间字符串转为时间戳
         * <p>格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param time 时间字符串
         * @return 毫秒时间戳
         */
        public static long string2Milliseconds(String time) {
            return string2Milliseconds(time, DEFAULT_SDF);
        }

        /**
         * 将时间字符串转为时间戳
         * <p>格式为用户自定义</p>
         *
         * @param time   时间字符串
         * @param format 时间格式
         * @return 毫秒时间戳
         */
        public static long string2Milliseconds(String time, SimpleDateFormat format) {
            try {
                return format.parse(time).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * 将时间字符串转为Date类型
         * <p>格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param time 时间字符串
         * @return Date类型
         */
        public static Date string2Date(String time) {
            return string2Date(time, DEFAULT_SDF);
        }

        /**
         * 将时间字符串转为Date类型
         * <p>格式为用户自定义</p>
         *
         * @param time   时间字符串
         * @param format 时间格式
         * @return Date类型
         */
        public static Date string2Date(String time, SimpleDateFormat format) {
            return new Date(string2Milliseconds(time, format));
        }

        /**
         * 将Date类型转为时间字符串
         * <p>格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param time Date类型时间
         * @return 时间字符串
         */
        public static String date2String(Date time) {
            return date2String(time, DEFAULT_SDF);
        }

        /**
         * 将Date类型转为时间字符串
         * <p>格式为用户自定义</p>
         *
         * @param time   Date类型时间
         * @param format 时间格式
         * @return 时间字符串
         */
        public static String date2String(Date time, SimpleDateFormat format) {
            return format.format(time);
        }

        /**
         * 将Date类型转为时间戳
         *
         * @param time Date类型时间
         * @return 毫秒时间戳
         */
        public static long date2Milliseconds(Date time) {
            return time.getTime();
        }

        /**
         * 将时间戳转为Date类型
         *
         * @param milliseconds 毫秒时间戳
         * @return Date类型时间
         */
        public static Date milliseconds2Date(long milliseconds) {
            return new Date(milliseconds);
        }

        /**
         * 毫秒时间戳单位转换（单位：unit）
         *
         * @param milliseconds 毫秒时间戳
         * @param unit         <ul>
         *                     <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}: 毫秒</li>
         *                     <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }: 秒</li>
         *                     <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }: 分</li>
         *                     <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}: 小时</li>
         *                     <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }: 天</li>
         *                     </ul>
         * @return unit时间戳
         */
        private static long milliseconds2Unit(long milliseconds, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit) {
            switch (unit) {
                case MSEC:
                    return milliseconds / MSEC;
                case SEC:
                    return milliseconds / SEC;
                case MIN:
                    return milliseconds / MIN;
                case HOUR:
                    return milliseconds / HOUR;
                case DAY:
                    return milliseconds / DAY;
            }
            return -1;
        }

        /**
         * 获取两个时间差（单位：unit）
         * <p>time1和time2格式都为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param time0 时间字符串1
         * @param time1 时间字符串2
         * @param unit  <ul>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}: 毫秒</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }: 秒</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }: 分</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}: 小时</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }: 天</li>
         *              </ul>
         * @return unit时间戳
         */
        public static long getIntervalTime(String time0, String time1, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit) {
            return getIntervalTime(time0, time1, unit, DEFAULT_SDF);
        }

        /**
         * 获取两个时间差（单位：unit）
         * <p>time1和time2格式都为format</p>
         *
         * @param time0  时间字符串1
         * @param time1  时间字符串2
         * @param unit   <ul>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}: 毫秒</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }: 秒</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }: 分</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}: 小时</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }: 天</li>
         *               </ul>
         * @param format 时间格式
         * @return unit时间戳
         */
        public static long getIntervalTime(String time0, String time1, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit, SimpleDateFormat format) {
            return Math.abs(milliseconds2Unit(string2Milliseconds(time0, format)
                    - string2Milliseconds(time1, format), unit));
        }

        /**
         * 获取两个时间差（单位：unit）
         * <p>time1和time2都为Date类型</p>
         *
         * @param time0 Date类型时间1
         * @param time1 Date类型时间2
         * @param unit  <ul>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}: 毫秒</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }: 秒</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }: 分</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}: 小时</li>
         *              <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }: 天</li>
         *              </ul>
         * @return unit时间戳
         */
        public static long getIntervalTime(Date time0, Date time1, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit) {
            return Math.abs(milliseconds2Unit(date2Milliseconds(time1)
                    - date2Milliseconds(time0), unit));
        }

        /**
         * 获取当前时间
         *
         * @return 毫秒时间戳
         */
        public static long getCurTimeMills() {
            return System.currentTimeMillis();
        }

        /**
         * 获取当前时间
         * <p>格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @return 时间字符串
         */
        public static String getCurTimeString() {
            return date2String(new Date());
        }

        /**
         * 获取当前时间
         * <p>格式为用户自定义</p>
         *
         * @param format 时间格式
         * @return 时间字符串
         */
        public static String getCurTimeString(SimpleDateFormat format) {
            return date2String(new Date(), format);
        }

        /**
         * 获取当前时间
         * <p>Date类型</p>
         *
         * @return Date类型时间
         */
        public static Date getCurTimeDate() {
            return new Date();
        }

        /**
         * 获取与当前时间的差（单位：unit）
         * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param time 时间字符串
         * @param unit <ul>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}:毫秒</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }:秒</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }:分</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}:小时</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }:天</li>
         *             </ul>
         * @return unit时间戳
         */
        public static long getIntervalByNow(String time, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit) {
            return getIntervalByNow(time, unit, DEFAULT_SDF);
        }

        /**
         * 获取与当前时间的差（单位：unit）
         * <p>time格式为format</p>
         *
         * @param time   时间字符串
         * @param unit   <ul>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}: 毫秒</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }: 秒</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }: 分</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}: 小时</li>
         *               <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }: 天</li>
         *               </ul>
         * @param format 时间格式
         * @return unit时间戳
         */
        public static long getIntervalByNow(String time, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit, SimpleDateFormat format) {
            return getIntervalTime(getCurTimeString(), time, unit, format);
        }

        /**
         * 获取与当前时间的差（单位：unit）
         * <p>time为Date类型</p>
         *
         * @param time Date类型时间
         * @param unit <ul>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MSEC}: 毫秒</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#SEC }: 秒</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#MIN }: 分</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#HOUR}: 小时</li>
         *             <li>{@link com.blankj.utilcode.utils.ConstUtils.TimeUnit#DAY }: 天</li>
         *             </ul>
         * @return unit时间戳
         */
        public static long getIntervalByNow(Date time, com.blankj.utilcode.utils.ConstUtils.TimeUnit unit) {
            return getIntervalTime(getCurTimeDate(), time, unit);
        }

        /**
         * 判断闰年
         *
         * @param year 年份
         * @return {@code true}: 闰年<br>{@code false}: 平年
         */
        public static boolean isLeapYear(int year) {
            return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/2
     *     desc  : 未归类工具类
     * </pre>
     */
    public static class UnclassifiedUtils {

        private UnclassifiedUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 获取服务是否开启
         *
         * @param context   上下文
         * @param className 完整包名的服务类名
         * @return {@code true}: 是<br>{@code false}: 否
         */
        public static boolean isRunningService(Context context, String className) {
            // 进程的管理者,活动的管理者
            ActivityManager activityManager = (ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE);
            // 获取正在运行的服务，最多获取1000个
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
            // 遍历集合
            for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
                ComponentName service = runningServiceInfo.service;
                if (className.equals(service.getClassName())) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * <pre>
     *     author: Blankj
     *     blog  : http://blankj.com
     *     time  : 2016/8/27
     *     desc  : 压缩相关工具类
     * </pre>
     */
    public static class ZipUtils {

        private ZipUtils() {
            throw new UnsupportedOperationException("u can't fuck me...");
        }

        /**
         * 批量压缩文件
         *
         * @param resFiles    待压缩文件集合
         * @param zipFilePath 压缩文件路径
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFiles(Collection<File> resFiles, String zipFilePath)
                throws IOException {
            return zipFiles(resFiles, zipFilePath, null);
        }

        /**
         * 批量压缩文件
         *
         * @param resFiles    待压缩文件集合
         * @param zipFilePath 压缩文件路径
         * @param comment     压缩文件的注释
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFiles(Collection<File> resFiles, String zipFilePath, String comment)
                throws IOException {
            return zipFiles(resFiles, com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath), comment);
        }

        /**
         * 批量压缩文件
         *
         * @param resFiles 待压缩文件集合
         * @param zipFile  压缩文件
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFiles(Collection<File> resFiles, File zipFile)
                throws IOException {
            return zipFiles(resFiles, zipFile, null);
        }

        /**
         * 批量压缩文件
         *
         * @param resFiles 待压缩文件集合
         * @param zipFile  压缩文件
         * @param comment  压缩文件的注释
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFiles(Collection<File> resFiles, File zipFile, String comment)
                throws IOException {
            if (resFiles == null || zipFile == null) return false;
            ZipOutputStream zos = null;
            try {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                for (File resFile : resFiles) {
                    if (!zipFile(resFile, "", zos, comment)) return false;
                }
                return true;
            } finally {
                if (zos != null) {
                    zos.finish();
                    com.blankj.utilcode.utils.FileUtils.closeIO(zos);
                }
            }
        }

        /**
         * 压缩文件
         *
         * @param resFilePath 待压缩文件路径
         * @param zipFilePath 压缩文件路径
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFile(String resFilePath, String zipFilePath)
                throws IOException {
            return zipFile(resFilePath, zipFilePath, null);
        }

        /**
         * 压缩文件
         *
         * @param resFilePath 待压缩文件路径
         * @param zipFilePath 压缩文件路径
         * @param comment     压缩文件的注释
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFile(String resFilePath, String zipFilePath, String comment)
                throws IOException {
            return zipFile(com.blankj.utilcode.utils.FileUtils.getFileByPath(resFilePath), com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath), comment);
        }

        /**
         * 压缩文件
         *
         * @param resFile 待压缩文件
         * @param zipFile 压缩文件
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFile(File resFile, File zipFile)
                throws IOException {
            return zipFile(resFile, zipFile, null);
        }

        /**
         * 压缩文件
         *
         * @param resFile 待压缩文件
         * @param zipFile 压缩文件
         * @param comment 压缩文件的注释
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        public static boolean zipFile(File resFile, File zipFile, String comment)
                throws IOException {
            if (resFile == null || zipFile == null) return false;
            ZipOutputStream zos = null;
            try {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                return zipFile(resFile, "", zos, comment);
            } finally {
                if (zos != null) {
                    zos.finish();
                    com.blankj.utilcode.utils.FileUtils.closeIO(zos);
                }
            }
        }

        /**
         * 压缩文件
         *
         * @param resFile  待压缩文件
         * @param rootPath 相对于压缩文件的路径
         * @param zos      压缩文件输出流
         * @param comment  压缩文件的注释
         * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
         * @throws IOException IO错误时抛出
         */
        private static boolean zipFile(File resFile, String rootPath, ZipOutputStream zos, String comment)
                throws IOException {
            rootPath = rootPath + (com.blankj.utilcode.utils.StringUtils.isSpace(rootPath) ? "" : File.separator) + resFile.getName();
            if (resFile.isDirectory()) {
                File[] fileList = resFile.listFiles();
                // 如果是空文件夹那么创建它，我把'/'换为File.separator测试就不成功，eggPain
                if (fileList.length <= 0) {
                    ZipEntry entry = new ZipEntry(rootPath + '/');
                    if (!com.blankj.utilcode.utils.StringUtils.isEmpty(comment)) entry.setComment(comment);
                    zos.putNextEntry(entry);
                    zos.closeEntry();
                } else {
                    for (File file : fileList) {
                        // 如果递归返回false则返回false
                        if (!zipFile(file, rootPath, zos, comment)) return false;
                    }
                }
            } else {
                InputStream is = null;
                try {
                    is = new BufferedInputStream(new FileInputStream(resFile));
                    ZipEntry entry = new ZipEntry(rootPath);
                    if (!com.blankj.utilcode.utils.StringUtils.isEmpty(comment)) entry.setComment(comment);
                    zos.putNextEntry(entry);
                    byte buffer[] = new byte[KB];
                    int len;
                    while ((len = is.read(buffer, 0, KB)) != -1) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                } finally {
                    com.blankj.utilcode.utils.FileUtils.closeIO(is);
                }
            }
            return true;
        }

        /**
         * 批量解压文件
         *
         * @param zipFiles    压缩文件集合
         * @param destDirPath 目标目录路径
         * @return {@code true}: 解压成功<br>{@code false}: 解压失败
         * @throws IOException IO错误时抛出
         */
        public static boolean unzipFiles(Collection<File> zipFiles, String destDirPath)
                throws IOException {
            return unzipFiles(zipFiles, com.blankj.utilcode.utils.FileUtils.getFileByPath(destDirPath));
        }

        /**
         * 批量解压文件
         *
         * @param zipFiles 压缩文件集合
         * @param destDir  目标目录
         * @return {@code true}: 解压成功<br>{@code false}: 解压失败
         * @throws IOException IO错误时抛出
         */
        public static boolean unzipFiles(Collection<File> zipFiles, File destDir)
                throws IOException {
            if (zipFiles == null || destDir == null) return false;
            for (File zipFile : zipFiles) {
                if (!unzipFile(zipFile, destDir)) return false;
            }
            return true;
        }

        /**
         * 解压文件
         *
         * @param zipFilePath 待解压文件路径
         * @param destDirPath 目标目录路径
         * @return {@code true}: 解压成功<br>{@code false}: 解压失败
         * @throws IOException IO错误时抛出
         */
        public static boolean unzipFile(String zipFilePath, String destDirPath)
                throws IOException {
            return unzipFile(com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath), com.blankj.utilcode.utils.FileUtils.getFileByPath(destDirPath));
        }

        /**
         * 解压文件
         *
         * @param zipFile 待解压文件
         * @param destDir 目标目录
         * @return {@code true}: 解压成功<br>{@code false}: 解压失败
         * @throws IOException IO错误时抛出
         */
        public static boolean unzipFile(File zipFile, File destDir)
                throws IOException {
            return unzipFileByKeyword(zipFile, destDir, null) != null;
        }

        /**
         * 解压带有关键字的文件
         *
         * @param zipFilePath 待解压文件路径
         * @param destDirPath 目标目录路径
         * @param keyword     关键字
         * @return 返回带有关键字的文件链表
         * @throws IOException IO错误时抛出
         */
        public static List<File> unzipFileByKeyword(String zipFilePath, String destDirPath, String keyword)
                throws IOException {
            return unzipFileByKeyword(com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath),
                    com.blankj.utilcode.utils.FileUtils.getFileByPath(destDirPath), keyword);
        }

        /**
         * 解压带有关键字的文件
         *
         * @param zipFile 待解压文件
         * @param destDir 目标目录
         * @param keyword 关键字
         * @return 返回带有关键字的文件链表
         * @throws IOException IO错误时抛出
         */
        public static List<File> unzipFileByKeyword(File zipFile, File destDir, String keyword)
                throws IOException {
            if (zipFile == null || destDir == null) return null;
            List<File> files = new ArrayList<>();
            ZipFile zf = new ZipFile(zipFile);
            Enumeration<?> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (com.blankj.utilcode.utils.StringUtils.isEmpty(keyword) || com.blankj.utilcode.utils.FileUtils.getFileName(entryName).toLowerCase().contains(keyword.toLowerCase())) {
                    String filePath = destDir + File.separator + entryName;
                    File file = new File(filePath);
                    files.add(file);
                    if (entry.isDirectory()) {
                        if (!com.blankj.utilcode.utils.FileUtils.createOrExistsDir(file)) return null;
                    } else {
                        if (!com.blankj.utilcode.utils.FileUtils.createOrExistsFile(file)) return null;
                        InputStream in = null;
                        OutputStream out = null;
                        try {
                            in = new BufferedInputStream(zf.getInputStream(entry));
                            out = new BufferedOutputStream(new FileOutputStream(file));
                            byte buffer[] = new byte[KB];
                            int len;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                        } finally {
                            com.blankj.utilcode.utils.FileUtils.closeIO(in, out);
                        }
                    }
                }
            }
            return files;
        }

        /**
         * 获取压缩文件中的文件路径链表
         *
         * @param zipFilePath 压缩文件路径
         * @return 压缩文件中的文件路径链表
         * @throws IOException IO错误时抛出
         */
        public static List<String> getFilesPath(String zipFilePath)
                throws IOException {
            return getFilesPath(com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath));
        }

        /**
         * 获取压缩文件中的文件路径链表
         *
         * @param zipFile 压缩文件
         * @return 压缩文件中的文件路径链表
         * @throws IOException IO错误时抛出
         */
        public static List<String> getFilesPath(File zipFile)
                throws IOException {
            if (zipFile == null) return null;
            List<String> paths = new ArrayList<>();
            Enumeration<?> entries = getEntries(zipFile);
            while (entries.hasMoreElements()) {
                paths.add(((ZipEntry) entries.nextElement()).getName());
            }
            return paths;
        }

        /**
         * 获取压缩文件中的注释链表
         *
         * @param zipFilePath 压缩文件路径
         * @return 压缩文件中的注释链表
         * @throws IOException IO错误时抛出
         */
        public static List<String> getComments(String zipFilePath)
                throws IOException {
            return getComments(com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath));
        }


        /**
         * 获取压缩文件中的注释链表
         *
         * @param zipFile 压缩文件
         * @return 压缩文件中的注释链表
         * @throws IOException IO错误时抛出
         */
        public static List<String> getComments(File zipFile)
                throws IOException {
            if (zipFile == null) return null;
            List<String> comments = new ArrayList<>();
            Enumeration<?> entries = getEntries(zipFile);
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                comments.add(entry.getComment());
            }
            return comments;
        }

        /**
         * 获取压缩文件中的文件对象
         *
         * @param zipFilePath 压缩文件路径
         * @return 压缩文件中的文件对象
         * @throws IOException IO错误时抛出
         */
        public static Enumeration<?> getEntries(String zipFilePath)
                throws IOException {
            return getEntries(com.blankj.utilcode.utils.FileUtils.getFileByPath(zipFilePath));
        }

        /**
         * 获取压缩文件中的文件对象
         *
         * @param zipFile 压缩文件
         * @return 压缩文件中的文件对象
         * @throws IOException IO错误时抛出
         */
        public static Enumeration<?> getEntries(File zipFile)
                throws IOException {
            if (zipFile == null) return null;
            return new ZipFile(zipFile).entries();
        }
    }

    public static class PhotoGet {
        private View avatorView;
        private String headIconPath;
        private static final int TAKEPHOTO = 1; // 拍照
        private static final int GALLERY = 2; // 从相册中选择
        private static final int PHOTO_REQUEST_CUT = 3; // 结果
        private File headFile;
        private String headBase64;
        private Context context;
        private BaseDialog dialog;
        public static final String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "ImageCache"
                + File.separator
                + "qingchundao"
                + File.separator;
        private static PhotoGet mInstance;

        private PhotoGet() {
        }

        public File getHeadFile() {
            return headFile;
        }

        public void setHeadFile(File headFile) {
            this.headFile = headFile;
        }

        public static PhotoGet getInstance() {
            if (mInstance == null) {
                mInstance = new PhotoGet();
            }
            return mInstance;
        }

        public void showAvatarDialog(Context context, BaseDialog dialog) {
            this.context = context;
            this.dialog = dialog;
            /**
             //         * 头像选择
             //         */

            avatorView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_modify_avator, null);
            avatorView.findViewById(R.id.photo_pop_tv_capture).setOnClickListener(new PhotoGet.PhotoOnClickListener());
            avatorView.findViewById(R.id.photo_pop_tv_album).setOnClickListener(new PhotoGet.PhotoOnClickListener());
            avatorView.findViewById(R.id.photo_pop_tv_cancel).setOnClickListener(new PhotoGet.PhotoOnClickListener());

            dialog.show(avatorView);
        }
        public void setContext(Context context){
            this.context=context;
        }


        class PhotoOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.photo_pop_tv_capture:
                        dialog.dismiss();
                        //startCameraPicCut();
                        dispatchTakePictureIntent();
                        break;
                    case R.id.photo_pop_tv_album:
                        dialog.dismiss();
                        //startImageCaptrue();
                        Crop.pickImage((Activity) context);
                        break;
                    case R.id.photo_pop_tv_cancel:
                        dialog.dismiss();
                        break;
                }
            }
        }

        private void startCameraPicCut() {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                String imageSavePath = SAVED_IMAGE_DIR_PATH;
                File dir = new File(imageSavePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // 调用系统的拍照功能
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra("camerasensortype", 2); // 调用前置摄像头
                intent.putExtra("autofocus", true); // 自动对焦
                intent.putExtra("fullScreen", false); // 全屏
                intent.putExtra("showActionIcons", false);
                // 指定调用相机拍照后照片的储存路径
                headIconPath = imageSavePath + File.separator + "userHeader" + ".jpg";
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(headIconPath)));
                ((Activity) context).startActivityForResult(intent, TAKEPHOTO);
            } else {
                //ToastUtil.showToast("请确认已经插入SD卡");
                Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }

        public String getHeadIconPath() {
            if (headIconPath != null)
                return headIconPath;
            else
                return null;
        }

        private void startImageCaptrue() {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            ((Activity) context).startActivityForResult(intent, GALLERY);
        }

        public void startPhotoZoom(Uri uri, int size) {


            PermissionsHelper.verifyStoragePermissions((Activity) context);

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            intent.putExtra("return-data", true);

            ((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_CUT);


        }

        Bitmap mHeaderImg;

        public Bitmap getGeadBitmap() {
            return mHeaderImg;
        }


        // 将进行剪裁后的图片上传
        public void saveImage(Intent picdata) {
            Bundle bundle = picdata.getExtras();

            if (bundle == null) {
                bundle = picdata.getBundleExtra("data");
                if (bundle == null) {
                    Log.i("gqf", "bundle==null2");
                }
            }

            if (bundle != null) {
                final Bitmap header = bundle.getParcelable("data");
                //final Bitmap header=PermissionsChecker.getImageBitmap(context,picdata);
                mHeaderImg = header;
                //保存图片到本地
                headFile = new File(BaseViewUtils.getFileSavePath(context) + "head.png");
                // 将头像显示出来

                //headBase64 = bitmaptoString(header, 100);
                headBase64 = "data:image/jpeg;base64,";
                headBase64 = headBase64 + bitmaptoString(header, 100);

                BaseViewUtils.saveBitmap(header, headFile);

                dialog = null;
            }
        }

        public void imgViewShowImg(Intent picdata, ImageView imageView) {
            Bundle bundle = picdata.getExtras();
            if (bundle != null) {
                Bitmap header = bundle.getParcelable("data");
                imageView.setImageBitmap(header);
            }
        }

        /**
         * 　　* 将bitmap转换成base64字符串
         * 　　* @param bitmap
         * 　　* @return base64 字符串
         */
        public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
            // 将Bitmap转换成字符串
            String string = null;
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
            byte[] bytes = bStream.toByteArray();
            string = Base64.encodeToString(bytes, Base64.NO_WRAP);
            return string;
        }

        public void beginCrop(Uri source) {
            Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped.jpg"));
            Crop.of(source, destination).withAspect(2,1).asSquare().start((Activity) context);
        }
        public void beginImgCrop(String path) {
            Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped.jpg"));
            Uri source=Uri.fromFile(new File(path));
            Crop.of(source, destination).withAspect(16,9).withMaxSize(320,180)
                    .start((Activity) context);
        }

        public void handleCrop(int resultCode, Intent result) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = Crop.getOutput(result);
                try {
                    headFile = new File(new URI(uri.toString()));
                    mHeaderImg = BitmapFactory.decodeFile(headFile.getAbsolutePath());
                    Log.i("gqf", "getName" + headFile.getName());
                } catch (Exception e) {
                } finally {

                }
                //headFile.renameTo(new File(headFile.getAbsolutePath()+  "head.png"));
                if (mHeaderImg == null) {
                    Log.i("gqf", "mHeaderImg==null");
                }
                //BaseViewUtils.saveBitmap(mHeaderImg, headFile);

            } else if (resultCode == Crop.RESULT_ERROR) {
                //Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("gqf", Crop.getError(result).getMessage());
            }
        }

        // 图片路径
        private Uri mCurrentPhotoUri;

        public Uri getmCurrentPhotoUri() {
            return mCurrentPhotoUri;
        }

        public static final int REQUEST_IMAGE_CAPTURE = 6789;

        // 拍照
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Constan.log("拍照photoFile："+photoFile);
                Constan.log("拍照context："+context);

                if (photoFile != null) {
                    try {
                        Constan.log(" BuildConfig.APPLICATION_ID："+ BuildConfig.APPLICATION_ID);
                        Uri photoUri = FileProvider.getUriForFile(context,  BuildConfig.APPLICATION_ID+".fileprovider", photoFile);
                        Constan.log("拍照photoUri："+photoUri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        mCurrentPhotoUri = photoUri;
                        ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (Exception e) {
                        Constan.log(e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }

        // 创建图片路径
        private File createImageFile() throws IOException {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,      /* prefix */
                    ".jpg",             /* suffix */
                    storageDir          /* directory */
            );

            return image;
        }


        public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
            InputStream input = ac.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            if ((originalWidth == -1) || (originalHeight == -1))
                return null;
            //图片分辨率以480x800为标准
            float hh = 800f;//这里设置高度为800f
            float ww = 480f;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / ww);
            } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (originalHeight / hh);
            }
            if (be <= 0)
                be = 1;
            //比例压缩
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = be;//设置缩放比例
            bitmapOptions.inDither = true;//optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            input = ac.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();

            return compressImage(bitmap);//再进行质量压缩
        }

        public static Bitmap compressImage(Bitmap image) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            return bitmap;
        }
    }


}
