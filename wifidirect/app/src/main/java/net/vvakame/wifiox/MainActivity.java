package net.vvakame.wifiox;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

//import net.vvakame.wifiox.game.BoardFragment;
import net.vvakame.wifiox.wifidirect.WiFiDirectFragment;
import net.vvakame.wifiox.wifidirect.WiFiDirectFragment.Type;
import net.vvakame.wifiox.wifidirect.WiFiDirectFragment.WiFiDirectCallback;
import net.vvakame.wifiox.wifidirect.WiFiDirectFragment.WiFiDirectCallbackPicker;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * メイン画面のActivity.
 *
 * @author vvakame
 */
public class MainActivity extends Activity implements WiFiDirectCallbackPicker, OnClickListener {

    static final String TAG = MainActivity.class.getSimpleName();
    final MainActivity self = this;

    WiFiDirectFragment mWifiDirectFragment;
   // BoardFragment mBoardFragment;

    Button mConnectButton;

    WifiP2pDevice mTargetDevice;

    OutputStream mPairOutputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        { // WifiDirectFragmentの作成と登録
            mWifiDirectFragment = new WiFiDirectFragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(mWifiDirectFragment, "WifiDirectFragment");
            transaction.commit();
        }

       // mBoardFragment = (BoardFragment) getFragmentManager().findFragmentById(R.id.board_fragment);
        mConnectButton = (Button) findViewById(R.id.connect);

        findViewById(R.id.discover_peers).setOnClickListener(this);
        findViewById(R.id.connect).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mWifiDirectFragment.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discover_peers:
                // "相手を探す" ボタンが押されたらPeerを探す
                mWifiDirectFragment.discoverPeers();
                break;

            case R.id.connect:
                // "接続" ボタンが押されたら接続しにいく
                if (mTargetDevice != null) {
                    mWifiDirectFragment.connect(mTargetDevice);
                    mConnectButton.setEnabled(false);
                }
                break;

            case R.id.disconnect:
                // "終了" ボタンが押されたら接続を解除しにいく
                mWifiDirectFragment.disconnect();
                break;
            default:
                break;
        }
    }

   /* @Override
    public BoardEventCallback getBoardEventCallback() {
        return new BoardEventCallbackImpl();
    }*/

    @Override
    public WiFiDirectCallback getWifiDirectActionCallback() {
        return new WiFiDirectCallbackImpl();
    }

    /**
     * Wi-Fi Direct管理用Fragmentで発生したイベントのコールバックを受け取る.
     *
     * @author vvakame
     */
    class WiFiDirectCallbackImpl implements WiFiDirectCallback {

        Closeable mSocket;

        @Override
        public void onDeviceInfoChanged(WifiP2pDevice thisDevice) {
        }

        @Override
        public void onWiFiDirectEnabled() {
            Toast.makeText(self, "Wi-Fi Directは有効です", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWiFiDirectDisabled() {
            Toast.makeText(self, "Wi-Fi Directを有効にしてください", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPeersAvailable(List<WifiP2pDevice> deviceList) {
            // 複数の相手から接続先を選ぶのはUI的に大変なので今回は1デバイスが相手の時のみ接続する
            if (deviceList.size() == 1) {
                mTargetDevice = deviceList.get(0);

                // Peerを探した結果、接続可能な相手がいたら"接続"ボタンを有効にする.
                switch (mTargetDevice.status) {
                    case WifiP2pDevice.INVITED:
                    case WifiP2pDevice.AVAILABLE:
                        mConnectButton.setEnabled(true);
                        break;
                    case WifiP2pDevice.CONNECTED:
                    case WifiP2pDevice.FAILED:
                    case WifiP2pDevice.UNAVAILABLE:
                        mConnectButton.setEnabled(false);
                        break;
                    default:
                        Log.d(TAG, "Unknown");
                        break;
                }
            } else if (deviceList.size() == 0) {
                mConnectButton.setEnabled(false);
                Toast.makeText(self, "接続先がありません...", Toast.LENGTH_SHORT).show();
            } else {
                mConnectButton.setEnabled(false);
                Toast.makeText(self, "接続先が複数あります...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onOpenSocket(Type type, final InputStream is, OutputStream os, final Closeable socket) {
            // Wi-Fi Directの接続後、Socketを開いて成功したらGameを開始する.

            Toast.makeText(self, "接続成功！", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onDisconnect() {
            mConnectButton.setEnabled(false);
            Toast.makeText(self, "切断中...", Toast.LENGTH_SHORT).show();
            try {
                mPairOutputStream = null;
                if (mSocket != null) {
                    mSocket.close();
                }
                mSocket = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}