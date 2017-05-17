package tqm.bianfeng.com.tqm.User.release;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwBankActivity;
import tqm.bianfeng.com.tqm.pojo.result.ResultWithLink;

/**
 * Created by johe on 2017/5/17.
 */

public class ReleaseMyActivityActivity extends BaseActivity {

    @BindView(R.id.release_myactivity_toolbar)
    Toolbar releaseMyactivityToolbar;
    @BindView(R.id.company_name_edi)
    EditText companyNameEdi;
    @BindView(R.id.upload_logo_img_txt)
    TextView uploadLogoImgTxt;
    @BindView(R.id.logo_img1)
    ImageView logoImg1;
    @BindView(R.id.add_logo_img_img)
    ImageView addLogoImgImg;
    @BindView(R.id.action_undo)
    ImageButton actionUndo;
    @BindView(R.id.action_redo)
    ImageButton actionRedo;
    @BindView(R.id.action_bold)
    ImageButton actionBold;
    @BindView(R.id.action_italic)
    ImageButton actionItalic;
    @BindView(R.id.action_subscript)
    ImageButton actionSubscript;
    @BindView(R.id.action_superscript)
    ImageButton actionSuperscript;
    @BindView(R.id.action_strikethrough)
    ImageButton actionStrikethrough;
    @BindView(R.id.action_underline)
    ImageButton actionUnderline;
    @BindView(R.id.action_heading1)
    ImageButton actionHeading1;
    @BindView(R.id.action_heading2)
    ImageButton actionHeading2;
    @BindView(R.id.action_heading3)
    ImageButton actionHeading3;
    @BindView(R.id.action_heading4)
    ImageButton actionHeading4;
    @BindView(R.id.action_heading5)
    ImageButton actionHeading5;
    @BindView(R.id.action_heading6)
    ImageButton actionHeading6;
    @BindView(R.id.action_txt_color)
    ImageButton actionTxtColor;
    @BindView(R.id.action_bg_color)
    ImageButton actionBgColor;
    @BindView(R.id.action_indent)
    ImageButton actionIndent;
    @BindView(R.id.action_outdent)
    ImageButton actionOutdent;
    @BindView(R.id.action_align_left)
    ImageButton actionAlignLeft;
    @BindView(R.id.action_align_center)
    ImageButton actionAlignCenter;
    @BindView(R.id.action_align_right)
    ImageButton actionAlignRight;
    @BindView(R.id.action_insert_bullets)
    ImageButton actionInsertBullets;
    @BindView(R.id.action_insert_numbers)
    ImageButton actionInsertNumbers;
    @BindView(R.id.action_blockquote)
    ImageButton actionBlockquote;
    @BindView(R.id.action_insert_image)
    ImageButton actionInsertImage;
    @BindView(R.id.action_insert_link)
    ImageButton actionInsertLink;
    @BindView(R.id.action_insert_checkbox)
    ImageButton actionInsertCheckbox;
    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.preview)
    TextView mPreview;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.link_edi)
    EditText linkEdi;
    @BindView(R.id.link_txt_edi)
    EditText linkTxtEdi;
    @BindView(R.id.commit_text_link)
    TextView commitTextLink;
    @BindView(R.id.link_edi_lin)
    LinearLayout linkEdiLin;

    public void initEditor() {
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.DKGRAY);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                ywBankActivity.setActivityContent(text);
                //mPreview.setText(text);
            }
        });
    }


    int activityId = 0;
    public static final String ACTIVITY_ID = "activityId";
    ShowDialogAndLoading showDialogAndLoading;

    YwBankActivity ywBankActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_my_activity);
        ButterKnife.bind(this);
        setToolbar(releaseMyactivityToolbar, "发布活动信息");
        initEditor();
        ywBankActivity = new YwBankActivity();
        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
                showDialogAndLoading.showLoading("稍等。。", ReleaseMyActivityActivity.this);
                save();
            }

            @Override
            public void showAfter() {
                finish();
            }
        });
        activityId = getIntent().getIntExtra(ACTIVITY_ID, 0);
        Log.i("gqf", "activityId" + activityId);
        getActivity();
    }

    public void demodd() {
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });
    }

    @OnClick({R.id.commit_text_link, R.id.upload_logo_img_txt, R.id.add_logo_img_img, R.id.action_undo, R.id.action_redo, R.id.action_bold, R.id.action_italic, R.id.action_subscript, R.id.action_superscript, R.id.action_strikethrough, R.id.action_underline, R.id.action_heading1, R.id.action_heading2, R.id.action_heading3, R.id.action_heading4, R.id.action_heading5, R.id.action_heading6, R.id.action_txt_color, R.id.action_bg_color, R.id.action_indent, R.id.action_outdent, R.id.action_align_left, R.id.action_align_center, R.id.action_align_right, R.id.action_insert_bullets, R.id.action_insert_numbers, R.id.action_blockquote, R.id.action_insert_image, R.id.action_insert_link, R.id.action_insert_checkbox, R.id.commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_logo_img_txt:

                if (mLogoSelectPath != null) {
                    uplodLogoImg(mLogoSelectPath);
                } else {
                    Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.add_logo_img_img:
                isAddLogo = true;
                addImg();
                break;
            case R.id.action_undo:
                mEditor.undo();
                break;
            case R.id.action_redo:
                mEditor.redo();
                break;
            case R.id.action_bold:
                mEditor.setBold();
                break;
            case R.id.action_italic:
                mEditor.setItalic();
                break;
            case R.id.action_subscript:
                mEditor.setSubscript();
                break;
            case R.id.action_superscript:
                mEditor.setSuperscript();
                break;
            case R.id.action_strikethrough:
                mEditor.setStrikeThrough();
                break;
            case R.id.action_underline:
                mEditor.setUnderline();
                break;
            case R.id.action_heading1:
                mEditor.setHeading(1);
                break;
            case R.id.action_heading2:
                mEditor.setHeading(2);
                break;
            case R.id.action_heading3:
                mEditor.setHeading(3);
                break;
            case R.id.action_heading4:
                mEditor.setHeading(4);
                break;
            case R.id.action_heading5:
                mEditor.setHeading(5);
                break;
            case R.id.action_heading6:
                mEditor.setHeading(6);
                break;
            case R.id.action_txt_color:
                //设置文本颜色
                break;
            case R.id.action_bg_color:
                //设置背景颜色
                break;
            case R.id.action_indent:
                mEditor.setIndent();
                break;
            case R.id.action_outdent:
                mEditor.setOutdent();
                break;
            case R.id.action_align_left:
                mEditor.setAlignLeft();
                break;
            case R.id.action_align_center:
                mEditor.setAlignCenter();
                break;
            case R.id.action_align_right:
                mEditor.setAlignRight();
                break;
            case R.id.action_insert_bullets:
                mEditor.setBullets();
                break;
            case R.id.action_insert_numbers:
                mEditor.setNumbers();
                break;
            case R.id.action_blockquote:
                mEditor.setBlockquote();
                break;
            case R.id.action_insert_image:
                //设置图片
                //mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG", "");
                isAddTextImg = true;
                addImg();

                break;
            case R.id.action_insert_link:
                //设置连接
                //mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
                linkEdiLin.setVisibility(View.VISIBLE);

                break;
            case R.id.action_insert_checkbox:
                mEditor.insertTodo();
                break;
            case R.id.commit:
                if (!ywBankActivity.getActivityContent().equals("")
                        && !companyNameEdi.getText().toString().equals("")
                        && mLogoSelectPath != null&&ywBankActivity.getImageUrl()!=null) {
                    ywBankActivity.setActivityTitle(companyNameEdi.getText().toString());
                    ywBankActivity.setCreateUser(1);
                    if (activityId != 0) {
                        ywBankActivity.setActivityId(activityId);
                    }
                    showDialogAndLoading.showBeforeDialog(this, "确认发布吗", " ", "取消", "确定");
                } else {
                    Toast.makeText(this, "请完善信息后再发布", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.commit_text_link:
                if (!linkEdi.getText().toString().equals("") && !linkTxtEdi.getText().toString().equals("")) {

                    mEditor.insertLink(linkEdi.getText().toString(), linkTxtEdi.getText().toString());
                    linkEdiLin.setVisibility(View.GONE);
                } else {

                }

                break;
        }
    }

    private static final int REQUEST_IMAGE = 2;
    ArrayList<String> mLogoSelectPath;
    ArrayList<String> mTxtSelectPath;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    boolean isAddLogo = false;
    boolean isAddTextImg = false;

    public void addImg() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {

            MultiImageSelector multiImageSelector = MultiImageSelector.create(this)
                    .showCamera(true) // show camera or not. true by default
                    // max select image size, 9 by default. used width #.multi()
                    .single() // single mode
                    .multi(); // original select data set, used width #.multi()

            if (isAddLogo) {
                multiImageSelector.origin(mLogoSelectPath);
            } else {
                multiImageSelector.origin(mTxtSelectPath);
            }

            multiImageSelector.count(1);

            multiImageSelector.start(this, REQUEST_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                setImgInView(data);
            }
        }
    }

    public void setImgInView(Intent data) {
        if (isAddTextImg) {
            mTxtSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            //文本图片，直接上传
            showDialogAndLoading.showLoading("上传图片中", this);
            uplodTxtImg(mTxtSelectPath);
            isAddTextImg = false;
        } else if (isAddLogo) {
            mLogoSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            logoImg1.setImageBitmap(null);
            logoImg1.setVisibility(View.GONE);
            for (int i = 0; i < mLogoSelectPath.size(); i++) {
                logoImg1.setImageBitmap(BitmapFactory.decodeFile(mLogoSelectPath.get(i)));
                logoImg1.setVisibility(View.VISIBLE);
            }
            isAddLogo = false;
        }
    }

    public void uplodLogoImg(List<String> imgPaths) {
        Log.i("gqf", "imgPaths" + imgPaths.toString());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < imgPaths.size(); i++) {
            File f = new File(imgPaths.get(i));
            if (f != null) {
                Log.i("gqf", "File" + i);
                if (f.exists()) {
                    RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), f);
                    builder.addFormDataPart("zichifile" + i, f.getName() + i, photoRequestBody);
                }
            }
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody mb = builder.build();

        List<MultipartBody.Part> zichifile = new ArrayList<>();
        for (int i = 0; i < mb.size(); i++) {
            zichifile.add(mb.part(i));
        }
        uploadLogoImgTxt.setText("上传中");
        uploadLogoImgTxt.setEnabled(false);
        Subscription subscription = NetWork.getUserService().uploadImg(zichifile.get(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultWithLink>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        uploadLogoImgTxt.setText("上传");
                        uploadLogoImgTxt.setEnabled(true);
                        Log.i("gqf", "Throwable" + e.toString());
                    }

                    @Override
                    public void onNext(ResultWithLink strings) {
                        Log.i("gqf", "onNext" + strings.toString());
                        uploadLogoImgTxt.setText("已上传");
                        ywBankActivity.setImageUrl(strings.getLink());
                        uploadLogoImgTxt.setEnabled(false);
                        addLogoImgImg.setEnabled(false);
                    }
                });
        compositeSubscription.add(subscription);

    }

    public void uplodTxtImg(List<String> imgPaths) {
        Log.i("gqf", "imgPaths" + imgPaths.toString());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < imgPaths.size(); i++) {
            File f = new File(imgPaths.get(i));
            if (f != null) {
                Log.i("gqf", "File" + i);
                if (f.exists()) {
                    RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), f);
                    builder.addFormDataPart("zichifile" + i, f.getName() + i, photoRequestBody);
                }
            }
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody mb = builder.build();

        List<MultipartBody.Part> zichifile = new ArrayList<>();
        for (int i = 0; i < mb.size(); i++) {
            zichifile.add(mb.part(i));
        }

        Subscription subscription = NetWork.getUserService().upload(zichifile.get(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultWithLink>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "Throwable" + e.toString());
                        showDialogAndLoading.stopLoaading();
                        Toast.makeText(ReleaseMyActivityActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResultWithLink resultWithLink) {
                        Log.i("gqf", "onNext" + resultWithLink.toString());
                        mEditor.insertImage(resultWithLink.getLink(), "");
                        showDialogAndLoading.stopLoaading();
                    }
                });
        compositeSubscription.add(subscription);

    }

    public void getActivity() {

        Subscription subscription = NetWork.getUserService().getReleaseActivity(activityId, realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwBankActivity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.toString());
                    }

                    @Override
                    public void onNext(YwBankActivity ywBankActity) {
                        ywBankActivity = ywBankActity;
                        Log.i("gqf", "onNext" + ywBankActivity.toString());
                        companyNameEdi.setText(ywBankActivity.getActivityTitle());
                        mEditor.setHtml(ywBankActivity.getActivityContent());


                    }
                });
        compositeSubscription.add(subscription);
    }

    public void save() {

        Log.i("gqf", "ywBankActivity" + ywBankActivity.toString());
        Subscription subscription = NetWork.getUserService().saveReleaseActivity(
                ywBankActivity.getImageUrl(), ywBankActivity.getActivityTitle(), ywBankActivity.getInstitution(), ywBankActivity.getActivityContent(), realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.toString());
                        showDialogAndLoading.stopLoaading();
                        Toast.makeText(ReleaseMyActivityActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResultCode releaseResult) {
                        showDialogAndLoading.stopLoaading();
                        Log.i("gqf", "onNext" + releaseResult.toString());
                        if (releaseResult.getCode()==ResultCode.SECCESS) {
                            showDialogAndLoading.showAfterDialog(ReleaseMyActivityActivity.this, "发布成功", "可在猫舍中查看审核状态", "确定");
                        } else {
                            Toast.makeText(ReleaseMyActivityActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

}
