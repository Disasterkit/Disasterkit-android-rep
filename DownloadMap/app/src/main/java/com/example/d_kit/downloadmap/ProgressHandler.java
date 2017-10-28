package com.example.d_kit.downloadmap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class ProgressHandler extends Handler{
    public ProgressDialog progressDialog;
    public AsyncFileLoader asyncfiledownload;

    //ダウンロード中の％表示やダイアログの消去の動作
    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);
        if(asyncfiledownload.isCancelled()){
            progressDialog.dismiss();
        }
        else if(asyncfiledownload.getStatus() == AsyncTask.Status.FINISHED){
            progressDialog.dismiss();
        }else{
            progressDialog.setProgress(asyncfiledownload.getLoadedBytePercent());
            this.sendEmptyMessageDelayed(0, 100);
        }
    }
}

