package com.shouyubang.android.sybang.record;

import android.util.Log;

import com.shouyubang.android.sybang.presenters.AccountHelper;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.tencent.cos.utils.SHA1Utils;

/**
 * Created by bradyxiao on 2017/4/11.
 * author bradyxiao
 */
public class PutObjectTask {

    private static final String TAG = "PutObjectTask";

    /**
     * 简单文件上传 : <20M的文件，直接上传
     */
    public static void  putObjectForSamllFile(BizService bizService,
                                              String cosPath, String localPath){
        /** PutObjectRequest 请求对象 */
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        /** 设置Bucket */
        putObjectRequest.setBucket(bizService.bucket);
        /** 设置cosPath :远程路径*/
        putObjectRequest.setCosPath(cosPath);
        /** 设置srcPath: 本地文件的路径 */
        putObjectRequest.setSrcPath(localPath);
        /** 设置 insertOnly: 是否上传覆盖同名文件*/
        putObjectRequest.setInsertOnly("1");
        /** 设置sign: 签名，此处使用多次签名 */
        putObjectRequest.setSign(bizService.getSign());

        /** 设置sha: 是否上传文件时带上sha，一般不需要带*/
        //putObjectRequest.setSha(putObjectRequest.getsha());

        /** 设置listener: 结果回调 */
        putObjectRequest.setListener(new IUploadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, long currentSize, long totalSize) {
                long progress = ((long) ((100.00 * currentSize) / totalSize));
                Log.w(TAG,"progress =" + progress + "%");
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                String result = "上传出错： ret =" +cosResult.code + "; msg =" + cosResult.msg;
                Log.w(TAG,result);
            }

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                PutObjectResult putObjectResult = (PutObjectResult) cosResult;
                String result = (" 上传结果： ret=" + putObjectResult.code + "; msg =" + putObjectResult.msg + "\n") +
                        putObjectResult.access_url + "\n" +
                        putObjectResult.resource_path + "\n" +
                        putObjectResult.url;
                Log.w(TAG,result);
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                String result = "上传出错： ret =" +cosResult.code + "; msg =" + cosResult.msg;
                Log.w(TAG,result);
            }
        });
        /** 发送请求：执行 */
        bizService.cosClient.putObject(putObjectRequest);
    }

    /**
     * 大文件分片上传 : >=20M的文件，需要使用分片上传，否则会出错
     */
    public static void putObjectForLargeFile(final AccountHelper uploadHelper,
                                             BizService bizService,
                                             String cosPath, String localPath){
        Log.i(TAG, "upload video start");
        /** PutObjectRequest 请求对象 */
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        /** 设置Bucket */
        putObjectRequest.setBucket(bizService.bucket);
        /** 设置cosPath :远程路径*/
        putObjectRequest.setCosPath(cosPath);
        /** 设置srcPath: 本地文件的路径 */
        putObjectRequest.setSrcPath(localPath);
        /** 设置 insertOnly: 是否上传覆盖同名文件*/
        putObjectRequest.setInsertOnly("1");
        /** 设置sign: 签名，此处使用多次签名 */
        String sign = bizService.getSign();
        putObjectRequest.setSign(sign);
        Log.i(TAG, "get sign: "+sign);

        /** 设置sliceFlag: 是否开启分片上传 */
        putObjectRequest.setSliceFlag(true);
        /** 设置slice_size: 若使用分片上传，设置分片的大小 */
        putObjectRequest.setSlice_size(1024*1024);

        /** 设置sha: 是否上传文件时带上sha，一般带上sha*/
        putObjectRequest.setSha(SHA1Utils.getFileSha1(localPath));

        /** 设置listener: 结果回调 */
        putObjectRequest.setListener(new IUploadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, long currentSize, long totalSize) {
                long progress = ((long) ((100.00 * currentSize) / totalSize));
                Log.w(TAG,"progress =" + progress + "%");
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                String result = "上传出错： ret =" +cosResult.code + "; msg =" + cosResult.msg;
                Log.w(TAG,result);
            }

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                PutObjectResult putObjectResult = (PutObjectResult) cosResult;
                String result = ("上传结果： ret=" + putObjectResult.code + "; msg =" + putObjectResult.msg + "\n") +
                        putObjectResult.access_url + "\n" +
                        putObjectResult.resource_path + "\n" +
                        putObjectResult.url;
                Log.w(TAG,result);
                String path = putObjectResult.resource_path;
                new VideoInfoTask(uploadHelper, path).execute();
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                String result = "上传出错： ret =" +cosResult.code + "; msg =" + cosResult.msg;
                Log.w(TAG,result);
            }
        });
        /** 发送请求：执行 */
        bizService.cosClient.putObject(putObjectRequest);
    }
}
