package com.example.d_kit.downloadmap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private final int REQUEST_PERMISSION = 1000;
    private final String TAG = "DownloadSample";
    private Button loadStartButton;
    private ProgressDialog progressDialog;
    private Handler progressHandler;
    private  String MAP_URL ;
    private  final  String japanMAP ="http://download.mapsforge.org/maps/asia/japan.map";
    private AsyncFileLoader fileLoader;
    final File file = Environment.getExternalStorageDirectory();
    final File directory = new File(file.getAbsolutePath() + "/Download/");
    File outputFile = new File(directory, "japan.map");
    private HashMap<String, String> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressHandler = new ProgressHandler();

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }

        final List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

        Map<String, String> data = new HashMap<String, String>();
        data.put("title", "japan");
        data.put("comment", "803.74MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "北海道");
        data.put("comment", "93.3MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "東北地方");
        data.put("comment", "94.6MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "関東地方");
        data.put("comment", "129.7MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "中部地方");
        data.put("comment", "167.8MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "関西地方");
        data.put("comment", "86.2MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "中国地方");
        data.put("comment", "68.7");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "四国地方");
        data.put("comment", "34.5MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "九州地方");
        data.put("comment", "90.6MB");
        dataList.add(data);

        data = new HashMap<String, String>();
        data.put("title", "沖縄県");
        data.put("comment", "6.4MB");
        dataList.add(data);





        MySimpleAdapter adapter = new MySimpleAdapter(
                this,
                dataList,
                R.layout.row,
                new String[] { "title", "comment" },
                new int[] { android.R.id.text1,
                        android.R.id.text2 });

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);//


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (view.getId()) {
                    case R.id.button1:
                        List listValues = new ArrayList(dataList.get(position).values());
                        MAP_URL = "http://download.mapsforge.org/maps/asia/"+ listValues.get(1) +".map";

                        //Toast.makeText(MainActivity.this, listValues.get(1) + "の削除ボタンが押されました", Toast.LENGTH_SHORT).show();
                        if(outputFile.exists()){
                            //ダイアログで警告をだしダウンロードを開始しない

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("すでに日本地図が保存されています。"+
                                    "最初からダウンロードしますか？")
                                    .setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // ボタンをクリックしたときの動作
                                            initFileLoader();
                                            showDialog(0);
                                            progressDialog.setProgress(0);
                                            progressHandler.sendEmptyMessage(0);
                                        }
                                    });

                            builder.setNegativeButton("CANCEL" , null);

                            builder.show();

                        }
                        else {
                            initFileLoader();
                            showDialog(0);
                            progressDialog.setProgress(0);
                            progressHandler.sendEmptyMessage(0);
                        }
                        break;
                }
            }
        });

        /*
        loadStartButton = (Button)findViewById(R.id.loadStartButton);
        loadStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //すでにファイルがあった時の動作をここに↓
                if(outputFile.exists()){
                    //ダイアログで警告をだしダウンロードを開始しない

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("すでに日本地図が保存されています。"+
                            "最初からダウンロードしますか？")
                            .setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // ボタンをクリックしたときの動作
                                    initFileLoader();
                                    showDialog(0);
                                    progressDialog.setProgress(0);
                                    progressHandler.sendEmptyMessage(0);
                                }
                            });

                    builder.setNegativeButton("CANCEL" , null);

                    builder.show();

                }
                else {
                    initFileLoader();
                    showDialog(0);
                    progressDialog.setProgress(0);
                    progressHandler.sendEmptyMessage(0);
                }
            }
        });
        */

        progressHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(fileLoader.isCancelled()){
                    progressDialog.dismiss();
                    Log.d(TAG, "load canceled");
                }
                else if(fileLoader.getStatus() == AsyncTask.Status.FINISHED){
                    progressDialog.dismiss();
                }else{
                    progressDialog.setProgress(fileLoader.getLoadedBytePercent());
                    progressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };



    }

    @Override
    protected void onPause(){
        Log.d(TAG, "onPause");
        super.onPause();
        //cancelLoad();
    }


    @Override
    protected void onStop(){
        Log.d(TAG, "onStop");
        super.onStop();
        //cancelLoad();
    }


    private void cancelLoad()
    {
        if(fileLoader != null){
            fileLoader.cancel(true);
        }
    }

    private void initFileLoader()
    {

        /*
        final File file = Environment.getExternalStorageDirectory();
        final File directory = new File(file.getAbsolutePath() + "/Download/");
        */

        if(!directory.exists()){
            directory.mkdir();
        }

        //File outputFile = new File(directory, "japan.map");

        fileLoader = new AsyncFileLoader(this, MAP_URL, outputFile);
        fileLoader.execute();


   /*
    //内部メモリの領域を用いる場合
    File dataDir = this.getFilesDir();
    File directory = new File(dataDir.getAbsolutePath()+ "/Download");
    if(!directory.exists()){
      if (directory.mkdir()){
      }else{
        Toast ts = Toast.makeText(this, "ディレクトリ作成に失敗", Toast.LENGTH_LONG);
        ts.show();
      }
    }
    File outputFile = new File(directory, "gaza.map");
    _fileLoader = new AsyncFileLoader(this,MAP_URL, outputFile);
    //_fileLoader.execute();
    */

    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case 0:
                progressDialog = new ProgressDialog(this);
                // _progressDialog.setIcon(R.drawable.ic_launcher);
                progressDialog.setTitle("Downloading files..");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Hide", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "hide");
                    }
                });
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "cancel");
                        cancelLoad();
                    }
                });
        }
        return progressDialog;
    }

    // permissionの確認
    public void checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            //initFileLoader();
        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this, "アプリ実行に許可が必要です", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // initFileLoader();
                return;

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this, "何もできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}