package cheweibao.jason.com.cheweibao;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheweibao.jason.com.carPlace.Bean.carPlace;
import cheweibao.jason.com.carPlace.service.placeService;
import cheweibao.jason.com.carPlace.service.userService;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by JasonSong on 2017/5/1.
 */

public class PublishActivity extends AppCompatActivity {


    private String placeName;
    private String userId;
    private String owner;
    private String address;
    private String price;
    private BmobGeoPoint geoPoint;
    EditText nameEdit;
    EditText addressEdit;
    EditText priceEdit;
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        Bmob.initialize(this, "3447c5aa7fb9f5375bac2d0848471693");
        init();


    }

    public void init() {
        nameEdit = (EditText) findViewById(R.id.edit_palceName);
        addressEdit = (EditText) findViewById(R.id.edit_address);
        priceEdit = (EditText) findViewById(R.id.edit_price);
        Intent intent = getIntent();
        owner = intent.getStringExtra("userName");
        userId = intent.getStringExtra("userId");
        Button LoadButton = (Button) findViewById(R.id.load_but_publish);
        LoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeName = nameEdit.getText().toString();
                address = addressEdit.getText().toString();
                price = priceEdit.getText().toString();
                placeService.publish(owner, placeName, address, price, geoPoint, userId);
                finish();
            }
        });


        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
//            showLocation(location);
            geoPoint = new BmobGeoPoint(location.getLongitude(), location.getLatitude());

        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }


    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
//            showLocation(location);
            geoPoint = new BmobGeoPoint(location.getLongitude(), location.getLatitude());

        }
    };
}
