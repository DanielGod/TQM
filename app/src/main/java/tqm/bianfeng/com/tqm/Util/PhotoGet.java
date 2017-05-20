package tqm.bianfeng.com.tqm.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import tqm.bianfeng.com.tqm.Dialog.BaseDialog;
import tqm.bianfeng.com.tqm.R;

public class PhotoGet {
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

    //    @Overridea
    //    public void OnViewClick(View v) {
    //        switch (v.getId()){
    //            case R.id.tv_save:
    //               // String intro=et_intro.getText().toString().trim();
    ////                if (TextUtils.isEmpty(intro)) {
    ////                    et_intro.setError("店铺描述不能为空");
    ////                    et_intro.requestFocus();
    ////                    return;
    ////                }
    //                if(headBase64!=null){
    //                    TreeMap<String,String> treeMap1=new TreeMap<String, String>();
    //                    treeMap1.put("operation", "setlogo");
    //                    treeMap1.put("shop_logo", headBase64);
    //                    treeMap1.put("token", token);
    //                    setStoreInfo(treeMap1);
    //                }else {
    //                    showToast("请选择图片！");
    //                }
    ////                TreeMap<String,String> treeMap3=new TreeMap<String, String>();
    ////                treeMap3.put("operation", "setbase");
    ////                treeMap3.put("shop_desc", intro);
    ////                treeMap3.put("token", token);
    ////                setStoreInfo(treeMap3);
    //                break;
    //            case R.id.iv_storeicon:
    //                showAvatarDialog();
    //                break;
    //            case R.id.photo_pop_tv_capture:
    //                dialog.dismiss();
    //                startCameraPicCut();
    //                break;
    //            case R.id.photo_pop_tv_album:
    //                dialog.dismiss();
    //                startImageCaptrue();
    //                break;
    //            case R.id.photo_pop_tv_cancel:
    //                dialog.dismiss();
    //                break;
    //        }
    //    }

    public void showAvatarDialog(Context context, BaseDialog dialog) {
        this.context = context;
        this.dialog = dialog;
        /**
         //         * 头像选择
         //         */

        avatorView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_modify_avator, null);
        avatorView.findViewById(R.id.photo_pop_tv_capture).setOnClickListener(new PhotoOnClickListener());
        avatorView.findViewById(R.id.photo_pop_tv_album).setOnClickListener(new PhotoOnClickListener());
        avatorView.findViewById(R.id.photo_pop_tv_cancel).setOnClickListener(new PhotoOnClickListener());

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
        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(2,1).asSquare().start((Activity) context);
    }
    public void beginImgCrop(String path) {
        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
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

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(context, "tqm.bianfeng.com.tqm", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                mCurrentPhotoUri = photoUri;

                ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
