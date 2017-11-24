package com.android.rivchat.ui.DisplayMap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.rivchat.R;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;

public class MapActivity extends AppCompatActivity implements LocationListener
{
    private double[] lat;
    private double[] lng;
    private int[] sit;
    private int[] situ;
    private double[][] otherlocation;
    private  int i=0;

    private final static String TAG = MapActivity.class.getSimpleName();
    private final static String MAP_FILE = "japan.map";
    private LocationManager myLocationManager;
    private Location lastLocation;
    //private Location mylastLocation;
    private final static int PERMISSION_REQUEST_CODE = 1;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(this.getApplication());
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.maptoolbar);
        setSupportActionBar(toolbar);

        mapView = new MapView(this);
        setContentView(mapView);

        Intent intent = getIntent();
        lat = intent.getDoubleArrayExtra("lat");
        lng = intent.getDoubleArrayExtra("lng");
        sit = intent.getIntArrayExtra("sit");

        if (lat[0]!=0.0&&lng[0]!=0.0) {
            otherlocation=new double[2][2];
            for(i=0;i<2;i++){
                otherlocation[i][0]=0.0;
                otherlocation[i][1]=0.0;
            }
            situ=new int[2];
            for(i=0;i<2;i++){
                otherlocation[i][0]=lat[i];
                otherlocation[i][1]=lng[i];
                situ[i]=sit[i];
            }

        }



        // ロケーションマネージャのインスタンスを取得する

        //myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       // LocationProvider provider = myLocationManager.getProvider(LocationManager.GPS_PROVIDER);

        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setZoomLevelMin((byte) 7);
        mapView.setZoomLevelMax((byte) 20);

        //Log.d(TAG, "externalDir: " + Environment.getExternalStorageDirectory());

        // targetSDK 23 以上の場合、実行時にパーミッション確認を行う必要がある
        // https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android
        // https://developer.android.com/training/permissions/requesting.html
        if (Build.VERSION.SDK_INT >= 23) {
            final int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                displayMap(lastLocation);
            }
        }


    }


    @Override//最後の位置を記録する
    public void onStart() {
        super.onStart();


        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//デフォルト表示
            lastLocation = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//最後に受け取った現在位置
            if(lastLocation != null) {
                displayMap(lastLocation);
                otherMaker();
            }
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        else {
            otherMaker();
            displayMap(lastLocation);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1000) {
            // 現在位置の取得はrequestLocationUpdatesを実行する必要がありますが、パーミッションチェックをやれとエラーが出ます。
            // そこで、このメソッドに到達した時点ではすでにパーミッションが許可/拒否されていますので、引数でなくとも
            // heckSelfPermissionを実行すればエラーも解消されますし良いかなと思って、以下のようにしています。
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                Toast.makeText(this, "権限を取得できませんでした。", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
        if (myLocationManager != null){
            myLocationManager.removeUpdates(this);
        }
    }



    //現在地が更新されたとき呼び出される関数
    @Override
    public void onLocationChanged(Location location) {
        nowLocation(location);//現在地にマーカーを出力
        try {
            myLocationManager.removeUpdates(this);//gennz
        } catch(SecurityException e) {
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    @Override
    protected void onDestroy() {
        mapView.destroyAll();;
        AndroidGraphicFactory.clearResourceMemoryCache();

        super.onDestroy();
    }



    //マップの出力
    private void displayMap(Location location) {

        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache", mapView.getModel().displayModel.getTileSize(), 1f, mapView.getModel().frameBufferModel.getOverdrawFactor() );

        MapDataStore mds = new MapFile(new File(Environment.getExternalStorageDirectory() + "/Download/", MAP_FILE));
        TileRendererLayer trl = new TileRendererLayer(tileCache, mds, mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE) {
            @Override
            public boolean onLongPress(LatLong tapLatLong, Point layerXY, Point tapXY) {
                return setMarker(tapLatLong);//長押しした場所にマーカーを出力
            }
        };

        trl.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

        mapView.getLayerManager().getLayers().add(trl);

        if(location!=null) {
            mapView.setCenter(new LatLong(location.getLatitude(), location.getLongitude())); //現在地からマップスタート
            mapView.setZoomLevel((byte) 13);
            nowLocation(location);//現在地へのマーカーの出力
            //TUNODAMaker();
        }

        else {
            mapView.setCenter(new LatLong(35.835311, 139.689335)); //現在地がない場合角田の家からマップスタート
            mapView.setZoomLevel((byte) 13);
            //TUNODAMaker();//角田の家へのマーカー出力
        }

    }

    //長押しした場所にマーカーを設置
    private boolean setMarker(LatLong latlong) {

        Marker marker = createMarker(latlong, R.drawable.marker_red);
        mapView.getLayerManager().getLayers().add(marker);
        return true;
    }



    /*
    public void TUNODAMaker(){
        LatLong tunoda = new LatLong(otherlocation[0], otherlocation[1]);
        Marker marker = createBubbleMarker(tunoda, R.drawable.marker_green, "角田の家");
        mapView.getLayerManager().getLayers().add(marker);

    }
    */

    public void otherMaker(){
        for(int h=0;h<2;h++) {
            if (otherlocation[h][0]!=0.0 && otherlocation[h][1]!=0.0) {
                if (situ[h] == 0) {
                    Marker marker = createBubbleMarker(new LatLong(otherlocation[h][0], otherlocation[h][1]), R.drawable.marker_green, "空いています");
                    mapView.getLayerManager().getLayers().add(marker);
                }

                if (situ[h] == 1) {
                    //LatLong tunoda = new LatLong(lat[h], lng[h]);
                    Marker marker = createBubbleMarker(new LatLong(otherlocation[h][0], otherlocation[h][1]), R.drawable.marker_red, "混雑しています");
                    mapView.getLayerManager().getLayers().add(marker);
                }
            }
            else {
                break;
            }
        }

    }

    //現在位置にマーカーを設置
    public void nowLocation(Location location){
        LatLong mylocation = new LatLong(location.getLatitude(),location.getLongitude());
        Marker marker = createBubbleMarker(mylocation, R.drawable.maker_location, "現在位置");
        mapView.getLayerManager().getLayers().add(marker);

    }






    //マーカー作成
    //drawbleをbitmapへ変換
    private Marker createMarker(LatLong latlong, int resource) {
        Drawable drawable = getResources().getDrawable(resource);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        return new Marker(latlong, bitmap, 0, -bitmap.getHeight() / 2);
    }

    //drawableからbitmap変換。バブルマーカー作成
    private Marker createBubbleMarker(LatLong latlong, int resource, String text) {
        Drawable drawable = getResources().getDrawable(resource);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        return new MarkerWithBubble(latlong, bitmap, 0, -bitmap.getHeight() / 2, this.mapView, text);
    }

}
