package action.sth.locationshow;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String CoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    final String AccessWifi = Manifest.permission.ACCESS_WIFI_STATE;
    final String ChangeWifi = Manifest.permission.CHANGE_WIFI_STATE;

    TextView textLocation;
    ListView listWifi;

    WifiManager wifi;
    List<ScanResult> wifiScanList;
    int size = 0;

    ArrayList<String> arraylist = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLocation = (TextView) findViewById(R.id.textLocation);
        listWifi = (ListView) findViewById(R.id.listWifi);

        if (checkSelfPermission(CoarseLocation) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        if (checkSelfPermission(AccessWifi) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE}, 123);
        }

        if (checkSelfPermission(ChangeWifi) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, 123);
        }

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arraylist);
        listWifi.setAdapter(this.adapter);

        checkWifiStatus();
        //refreshLocation();
    }

    public void onClickRefreshButton(View view){
        refreshLocation();
    }

    public void checkWifiStatus(){
        if (!wifi.isWifiEnabled()){
            wifi.setWifiEnabled(true) ;
        }
        else
            return;
    }

    public String checkScanList(){
        if(arraylist.contains("a2:20:a6:21:b9:b5")){
            return "Test Station One";
        }
        else
            return "nowhere";
    }
    
    public void refreshLocation(){
        arraylist.clear();
        adapter.clear();
        checkWifiStatus();
        registerReceiver(wifi_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
        Log.d("WifScanner", "scanWifiNetworks");
    }

    BroadcastReceiver wifi_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WifiScanner","onReceive");
            wifiScanList = wifi.getScanResults();
            size = wifiScanList.size();
            unregisterReceiver(this);

            try{
                while (size >= 0){
                    size--;
                    arraylist.add(wifiScanList.get(size).BSSID);
                    adapter.notifyDataSetInvalidated();
                }
            }
            catch (Exception e){
                Log.w("WifiScanner","Exception:"+ e);
                String location = checkScanList();
                textLocation.setText(location);
            }
        }
    };
}
