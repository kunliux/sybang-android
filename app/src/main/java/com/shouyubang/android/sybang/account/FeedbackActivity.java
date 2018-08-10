package com.shouyubang.android.sybang.account;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.utils.DialogUtil;
import com.shouyubang.android.sybang.utils.PostUtil;
import com.shouyubang.android.sybang.wallet.WalletActivity;

import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KunLiu on 2017/3/21.
 */

public class FeedbackActivity extends AppCompatActivity {

    private String contact;
    private String content;

    @BindView(R.id.feedback_contact)
    EditText mFeedbackContact;
    @BindView(R.id.feedback_content)
    EditText mFeedbackContent;
    @BindView(R.id.feedback_submit_btn)
    Button mFeedbackSubmitBtn;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, FeedbackActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        mFeedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact=mFeedbackContact.getText().toString();
                content=mFeedbackContent.getText().toString();
                new PostItemTask().execute();
                //finish();
            }
        });
    }

    private class PostItemTask extends AsyncTask<Void,Void,Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int result= 0;
            try {
                result = new PostUtil().feedbackPost(Constants.FEEDBACK_API,contact,content);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(Integer result){
            if(result==200)
                DialogUtil.showDialog(getApplicationContext(), "感谢您的宝贵建议", false);
            else
                DialogUtil.showDialog(getApplicationContext(), "提交过程遇到异常，请稍后再试", false);

        }
    }


}
