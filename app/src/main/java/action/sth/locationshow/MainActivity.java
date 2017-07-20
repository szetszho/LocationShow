package action.sth.locationshow;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
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

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arraylist);
        listWifi.setAdapter(this.adapter);

        checkWifiStatus();
        //refreshLocation();
    }

    public void checkWifiStatus(){
        if (!wifi.isWifiEnabled()){
            wifi.setWifiEnabled(true) ;
        }
        else
            return;
    }

    public String checkScanList(){
        return "null";
    }

    public void refreshLocation(View view){
        arraylist.clear();
        checkWifiStatus();
        registerReceiver(wifi_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
        Log.d("WifScanner", "scanWifiNetworks");
        String location = checkScanList();
        textLocation.setText(location);
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
            }
        }
    };

}
