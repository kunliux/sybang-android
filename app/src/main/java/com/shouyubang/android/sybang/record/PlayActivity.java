package com.shouyubang.android.sybang.record;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shouyubang.android.sybang.MainActivity;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.presenters.AccountHelper;
import com.shouyubang.android.sybang.presenters.view.UploadVideoView;
import com.shouyubang.android.sybang.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends AppCompatActivity implements UploadVideoView {

    private static final String TAG = "PlayActivity";

    public final static String DATA = "URL";

    @BindView(R.id.playView)
    PlayView playView;
    @BindView(R.id.btn_play)
    Button playBtn;
    @BindView(R.id.btn_upload)
    Button uploadBtn;
    @BindView(R.id.activity_play)
    RelativeLayout activityPlay;

    private long playPosition = -1;
    private long duration = -1;
    String currentPath;
    BizService bizService;
    AccountHelper mUploadHelper;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mUploadHelper = new AccountHelper(getApplicationContext(), this);

        ButterKnife.bind(this);

        //初始化 cosClient
        bizService = BizService.instance();
        bizService.init(getApplicationContext());

        currentPath = getIntent().getStringExtra(DATA);

        playView.setVideoURI(Uri.parse(currentPath));

        playView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playView.seekTo(1);
                startVideo();
            }
        });

        playView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //获取视频资源的宽度
                int videoWidth = mp.getVideoWidth();
                //获取视频资源的高度
                int videoHeight = mp.getVideoHeight();
                playView.setSizeH(videoHeight);
                playView.setSizeW(videoWidth);
                playView.requestLayout();
                duration = mp.getDuration();
                play();
            }
        });

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (!isScreenOn) {
            pauseVideo();
        }
    }

    @OnClick({R.id.btn_play, R.id.btn_retake, R.id.btn_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                play();
                break;
            case R.id.btn_retake:
                retake();
                break;
            case R.id.btn_upload:
                upload();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (playPosition > 0) {
            pauseVideo();
        }
        playView.seekTo((int) ((playPosition > 0 && playPosition < duration) ? playPosition : 1));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playView.pause();
        playPosition = playView.getCurrentPosition();
        pauseVideo();

    }

    @Override
    public void onBackPressed() {
//        FileUtils.deleteFile(currentPath);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void pauseVideo() {
        playView.pause();
        playBtn.setText("回看");
    }

    private void startVideo() {
        playView.start();
        playBtn.setText("暂停");
    }

    /**
     * 播放
     */
    private void play() {
        if (playView.isPlaying()) {
            pauseVideo();
        } else {
            if (playView.getCurrentPosition() == playView.getDuration()) {
                playView.seekTo(0);
            }
            startVideo();
        }
    }

    /**
     * 重录
     */
    private void retake() {
        PlayActivity.this.finish();
    }

    /**
     * 上传
     */
    private void upload() {
        mProgressDialog = DialogUtil.getProgressDialog(PlayActivity.this, "上传视频中", false);
        mProgressDialog.show();
        mUploadHelper.upload(bizService, currentPath);
    }

    @Override
    public void uploadSuccess() {
        mProgressDialog.dismiss();
        Toast.makeText(PlayActivity.this, "上传视频成功", Toast.LENGTH_SHORT).show();
        jumpIntoVideoListActivity();
    }

    @Override
    public void uploadFail(String module, int errCode, String errMsg) {
        mProgressDialog.dismiss();
        Toast.makeText(PlayActivity.this, "上传视频失败", Toast.LENGTH_SHORT).show();
        uploadBtn.setClickable(true);
    }

    /**
     * 直接跳转主界面
     */
    private void jumpIntoVideoListActivity() {
        Intent intent = new Intent(PlayActivity.this, VideoListActivity.class);
        startActivity(intent);
        finish();
    }
}
