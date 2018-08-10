package com.shouyubang.android.sybang.record;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.CurVideoInfo;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.UploadHelper;
import com.shouyubang.android.sybang.presenters.view.UploadImageView;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.utils.UIUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播发布类
 */
public class PostVideoActivity extends AppCompatActivity implements View.OnClickListener, UploadImageView {

    private static final String TAG = "PostVideoActivity";

    private UploadHelper mUploadHelper;
    private TextView BtnPublish;
    private Dialog mPicChsDialog;
    private ImageView cover;
    private Uri fileUri, cropUri;
    private TextView tvPicTip;
    private TextView tvTitle;
    private static final int CAPTURE_IMAGE_CAMERA = 1;
    private static final int IMAGE_STORE = 2;
    private static final int CROP_CHOOSE = 3;

    private boolean bUploading = false;
    private boolean bPermission = false;
    private int uploadPercent = 0;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, PostVideoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);
        mUploadHelper = new UploadHelper(PostVideoActivity.this, this);
        tvTitle = (TextView) findViewById(R.id.live_title);
        tvPicTip = (TextView) findViewById(R.id.tv_pic_tip);
        BtnPublish = (TextView) findViewById(R.id.btn_publish);
        cover = (ImageView) findViewById(R.id.cover);
        cover.setOnClickListener(this);
        BtnPublish.setOnClickListener(this);

        initPhotoDialog();
        // 提前更新sig
        mUploadHelper.updateSig();

        bPermission = checkPublishPermission();
    }

    @Override
    protected void onDestroy() {
        mUploadHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_publish) {
            if (bUploading) {
                Toast.makeText(this, getString(R.string.publish_wait_uploading) + " " + uploadPercent + "%", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, RecordActivity.class);
                CurVideoInfo.getInstance().setTitle(tvTitle.getText().toString().isEmpty() ? "手语视频" : tvTitle.getText().toString());
                startActivity(intent);
                this.finish();
            }

        } else if (i == R.id.cover) {
            mPicChsDialog.show();
        }
    }

    /**
     * 图片选择对话框
     */
    private void initPhotoDialog() {
        mPicChsDialog = new Dialog(this, R.style.float_dialog);
        mPicChsDialog.setContentView(R.layout.pic_choose);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = mPicChsDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度

        mPicChsDialog.getWindow().setAttributes(lp);

        TextView camera = (TextView) mPicChsDialog.findViewById(R.id.chos_camera);
        TextView picLib = (TextView) mPicChsDialog.findViewById(R.id.pic_lib);
        TextView cancel = (TextView) mPicChsDialog.findViewById(R.id.btn_cancel);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFrom(CAPTURE_IMAGE_CAMERA);
                mPicChsDialog.dismiss();
            }
        });

        picLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFrom(IMAGE_STORE);
                mPicChsDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPicChsDialog.dismiss();
            }
        });
    }


    /**
     * 获取图片资源
     *
     * @param type
     */
    private void getPicFrom(int type) {
        if (!bPermission) {
            Toast.makeText(this, getString(R.string.tip_no_permission), Toast.LENGTH_SHORT).show();
            return;
        }

        switch (type) {
            case CAPTURE_IMAGE_CAMERA:
                fileUri = createCoverUri("", false);
                Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent_photo, CAPTURE_IMAGE_CAMERA);
                break;
            case IMAGE_STORE:
                fileUri = createCoverUri("_select", false);
                Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
                intent_album.setType("image/*");
                startActivityForResult(intent_album, IMAGE_STORE);
                break;

        }
    }

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(PostVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(PostVideoActivity.this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(PostVideoActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(PostVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(PostVideoActivity.this,
                        permissions.toArray(new String[0]),
                        Constants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }

        return true;
    }

    private Uri createCoverUri(String type, boolean bCrop) {
        String filename = MySelfInfo.getInstance().getId() + type + ".jpg";
        File outputImage = new File(Environment.getExternalStorageDirectory(), filename);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bCrop) {
            return Uri.fromFile(outputImage);
        }else {
            return UIUtils.getUriFromFile(this, outputImage);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE_CAMERA:
                    startPhotoZoom(fileUri);
                    break;
                case IMAGE_STORE:
                    String path = UIUtils.getPath(this, data.getData());
                    if (null != path) {
                        Log.d(TAG, "startPhotoZoom->path:" + path);
                        File file = new File(path);
                        startPhotoZoom(UIUtils.getUriFromFile(this, file));
                    }
                    break;
                case CROP_CHOOSE:
                    tvPicTip.setVisibility(View.GONE);
                    cover.setImageBitmap(null);
                    cover.setImageURI(cropUri);
                    bUploading = true;
                    Log.i(TAG, "Show me the Photo.");
                    mUploadHelper.uploadCover(cropUri.getPath());
                    break;

            }
        }

    }

    public void startPhotoZoom(Uri uri) {
        cropUri = createCoverUri("_crop", true);

        Intent intent = new Intent("com.android.camera.action.CROP");
        /* 这句要记得写：这是申请权限，之前因为没有添加这个，打开裁剪页面时，一直提示“无法修改低于50*50像素的图片”，
      开始还以为是图片的问题呢，结果发现是因为没有添加FLAG_GRANT_READ_URI_PERMISSION。
      如果关联了源码，点开FileProvider的getUriForFile()看看（下面有），注释就写着需要添加权限。
      */
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 500);
        intent.putExtra("aspectY", 300);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_CHOOSE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.WRITE_PERMISSION_REQ_CODE:
                for (int i=0; i<grantResults.length; i++){
                     if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "request permission failed: "+permissions[i]);
                     }else{
                         Log.d(TAG, "request permission success: "+permissions[i]);
                     }
                }
                bPermission = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onUploadResult(int code, String url) {
        if (code >= 0) {
            CurVideoInfo.getInstance().setCoverUrl(url);
            Toast.makeText(this, getString(R.string.publish_upload_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.publish_upload_cover_failed)+"|"+code+"|"+url, Toast.LENGTH_SHORT).show();
        }
        bUploading = false;
    }

    @Override
    public void onUploadProcess(int percent) {
        uploadPercent = percent;
    }
}
