package action.sth.locationshow;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager wifi;
    List<ScanResult> wifiScanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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

    public void refreshLocation(View view){
        wifi.startScan();
        wifiScanList = wifi.getScanResults();
        String location = checkScanList();
        TextView textLocation = (TextView) findViewById(R.id.textLocation);
        textLocation.setText(location);
    }

    public String checkScanList(){
        for (ScanResult result: wifiScanList){
            String a = result.BSSID;
            TextView textBSSID = (TextView)findViewById(R.id.textBSSID);
            textBSSID.append(a);
            textBSSID.append("||||");
        }
        return "somewhere";
    }

}
