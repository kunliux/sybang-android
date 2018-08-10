package com.shouyubang.android.sybang.call;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.shouyubang.android.sybang.MainActivity;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.Comment;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";

    private static final String CALL_ID = "callId";
    private static final String STAFF_ID = "staffId";

    @BindView(R.id.comment_text)
    EditText mCommentText;
    @BindView(R.id.comment_rating_bar)
    AppCompatRatingBar mCommentRatingBar;
    @BindView(R.id.comment_submit_button)
    Button mCommentSubmitButton;
    @BindView(R.id.comment_reward_check_box)
    CheckBox mCommentRewardCheckBox;
    @BindView(R.id.comment_anonymous_check_box)
    CheckBox mCommentAnonymousCheckBox;

    String callId = "";
    String staffId = "";

    public static Intent newIntent(Context context, String callId, String staffId) {
        Intent intent = new Intent();
        intent.setClass(context, CommentActivity.class);
        intent.putExtra(CALL_ID, callId);
        intent.putExtra(STAFF_ID, staffId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        callId = getIntent().getStringExtra(CALL_ID);
        staffId = getIntent().getStringExtra(STAFF_ID);
        ButterKnife.bind(this);
        mCommentSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });
    }
    

    private void submitComment() {
        if (!TextUtils.isEmpty(mCommentText.getText().toString()) || mCommentRatingBar.getRating() != 0) {
            Comment comment = new Comment();
            comment.setRating(Math.round(mCommentRatingBar.getRating() * 2));
            comment.setCallId(callId);
            comment.setIsAnonymous(mCommentAnonymousCheckBox.isChecked()? 1:0);
            comment.setUserId(MySelfInfo.getInstance().getId());
            comment.setStaffId(staffId);
            comment.setReward(mCommentRewardCheckBox.isChecked()? 100:0);
            comment.setContent(mCommentText.getText().toString().trim());
            comment.setTime(TimeUtil.getNowTime());
            new CommentTask(comment).execute();
        }
    }


    private class CommentTask extends AsyncTask<Void, Void, Boolean> {
        private Comment comment;

        CommentTask(Comment comment) {
            this.comment = comment;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.i(TAG, "User submit comment: " + comment.getContent());
            return UserServerHelper.submitComment(comment);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(CommentActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                jumpIntoMainActivity();
            }
        }

    }

    /**
     * 直接跳转主界面
     */
    private void jumpIntoMainActivity() {
        Intent intent = new Intent(CommentActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
