package action.sth.locationshow;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String CoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    final String AccessWifi = Manifest.permission.ACCESS_WIFI_STATE;
    final String ChangeWifi = Manifest.permission.CHANGE_WIFI_STATE;

    WifiManager wifi;
    List<ScanResult> wifiScanList;

    int size = 0;
    int index = 0;
    int level = 0;

    TextView textLocation;
    Button buttonRefresh;
    ListView listWifi;

    ArrayList<Integer> levelList = new ArrayList<>();

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter adapter;

    /*
    TextView textSsid;
    TextView textBssid;
    TextView textLevel;

    ArrayList<String> ssidList = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<Integer> levelList = new ArrayList<>(;)

    ArrayAdapter adapterSsid;
    ArrayAdapter adapter;
    ArrayAdapter adapterLevel;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPremission();

        setContentView(R.layout.activity_main);
        textLocation = (TextView) findViewById(R.id.textLocation);
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setClickable(false);
        listWifi = (ListView) findViewById(R.id.listWifi);

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listWifi.setAdapter(this.adapter);

        /*
        textSsid = (TextView) findViewById(R.id.textSsid);
        textBssid = (TextView) findViewById(R.id.textBssid);
        textLevel = (TextView) findViewById(R.id.textLevel);

        this.adapterSsid = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ssidList);
        listWifi.setAdapter(this.adapterSsid);
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listWifi.setAdapter(this.adapter);
        this.adapterLevel = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, levelList);
        listWifi.setAdapter(this.adapterLevel);
        */


        checkWifiStatus();
        refreshLocation();
    }

    public void onClickRefreshButton(View view){
        buttonRefresh.setClickable(false);
        refreshLocation();
    }

    public void checkPremission(){
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
    }

    public void checkWifiStatus(){
        if (!wifi.isWifiEnabled()){
            wifi.setWifiEnabled(true);
        }
        else
            return;
    }

    public String checkScanList(){
        if(arrayList.contains("a2:20:a6:21:b9:b5") && !arrayList.contains("f8:32:e4:79:41:b0")){
            return "Test Station One";
        }
        else if(arrayList.contains("f8:32:e4:79:41:b0")&& !arrayList.contains("a2:20:a6:21:b9:b5")){
            return "Test Station Two";
        }
        else if(arrayList.contains("a2:20:a6:21:b9:b5") && arrayList.contains("")){
            int a = arrayList.indexOf("a2:20:a6:21:b9:b5");
            int b = arrayList.indexOf("f8:32:e4:79:41:b0");
            int c = levelList.get(a);
            int d = levelList.get(b);
            if(c > d){
                return "Test Station One";
            }
            else if(d > c){
                return "Test Station Two";
            }
            else
                return "Middle of One and Two";
        }
        else
            return "nowhere";
    }
    
    public void refreshLocation(){
        arrayList.clear();
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
                    arrayList.add(wifiScanList.get(size).BSSID);
                    levelList.add(wifiScanList.get(size).level);
                    adapter.notifyDataSetInvalidated();
                }
            }
            catch (Exception e){
                Log.w("WifiScanner","Exception:"+ e);
                String location = checkScanList();
                textLocation.setText(location);
                buttonRefresh.setClickable(true);
            }
        }
    };
}
