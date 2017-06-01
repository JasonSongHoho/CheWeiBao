package cheweibao.jason.com.cheweibao;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by JasonSong on 2017/4/18.
 */

public class MainActivity extends AppCompatActivity {
    private Spinner spinner_kilometre;
    private Spinner spinner_limit;
    private String kilometre;
    private String limit;
    private String userName;
    private String password;
    private String userId;

    //    private String placeId;
    private ListView listView;
    private SimpleAdapter adapter;
    private BmobGeoPoint geoPoint;
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "3447c5aa7fb9f5375bac2d0848471693");
        initLocation();
        init();

    }

    private void initLocation() {

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

    private void init() {
        Intent getIntent = getIntent();
        userName = getIntent.getStringExtra("name");
        password = getIntent.getStringExtra("password");
        userId = getIntent.getStringExtra("userId");

        spinner_kilometre = (Spinner) findViewById(R.id.spinner_kiloamate);
        spinner_limit = (Spinner) findViewById(R.id.spinner_limit);


        BmobQuery<carPlace> bmobQuery = new BmobQuery<carPlace>();
        bmobQuery.addWhereEqualTo("available", true);
        bmobQuery.addWhereWithinKilometers("geoPoint", geoPoint, 10);
        bmobQuery.setLimit(10);    //获取最接近用户地点的limit 条数据
        bmobQuery.findObjects(new FindListener<carPlace>() {
            @Override
            public void done(List<carPlace> object, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "查询成功,共" + object.size() + "条数据。",
                            Toast.LENGTH_LONG).show();
                    List<carPlace> carPlaces = object;
                    //获取到集合数据
                    List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                    for (carPlace carPlace : carPlaces) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("id", carPlace.getObjectId());
                        item.put("name", carPlace.getName());
                        item.put("owner", carPlace.getOwner());
                        item.put("address", carPlace.getAddress());
                        item.put("price", carPlace.getPrice());
                        data.add(item);
                    }
                    //创建SimpleAdapter适配器将数据绑定到item显示控件上
                    adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.item,
                            new String[]{"id", "name", "owner", "address", "price"}, new int[]{R.id.carid_text, R.id.name_text, R.id.owner_text, R.id.address_text, R.id.price_text});
                    //实现列表的显示
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败:" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button userButton = (Button) findViewById(R.id.user_info_but);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UserInfoActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("password", password);
                startActivity(intent);

            }
        });
        Button publishButton = (Button) findViewById(R.id.publish_but);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PublishActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                startActivity(intent);

            }
        });
        Button publishedButton = (Button) findViewById(R.id.published_but);
        publishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PublishedActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                startActivity(intent);

            }
        });
        Button applyedButton = (Button) findViewById(R.id.applyed_but);
        applyedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ApplyedActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                startActivity(intent);

            }
        });

        listView = (ListView) this.findViewById(R.id.listView);

        //条目点击事件
        listView.setOnItemClickListener(new ItemClickListener());


        Button LoadButton = (Button) findViewById(R.id.load_but);
        LoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kilometre = spinner_kilometre.getSelectedItem().toString();
                limit = spinner_limit.getSelectedItem().toString();
                BmobQuery<carPlace> bmobQuery = new BmobQuery<carPlace>();
                bmobQuery.addWhereWithinKilometers("geoPoint", geoPoint, Integer.parseInt(kilometre));
                bmobQuery.addWhereEqualTo("available", true);
                bmobQuery.setLimit(Integer.parseInt(limit));    //获取最接近用户地点的limit 条数据
                bmobQuery.findObjects(new FindListener<carPlace>() {
                    @Override
                    public void done(List<carPlace> object, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "查询成功,共" + object.size() + "条数据。",
                                    Toast.LENGTH_LONG).show();
                            List<carPlace> carPlaces = object;
                            //获取到集合数据
                            List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                            for (carPlace carPlace : carPlaces) {
                                HashMap<String, Object> item = new HashMap<String, Object>();
                                item.put("id", carPlace.getObjectId());
                                item.put("name", carPlace.getName());
                                item.put("owner", carPlace.getOwner());
                                item.put("address", carPlace.getAddress());
                                item.put("price", carPlace.getPrice());
                                data.add(item);
                            }
                            //创建SimpleAdapter适配器将数据绑定到item显示控件上
                            adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.item,
                                    new String[]{"id", "name", "owner", "address", "price"}, new int[]{R.id.carid_text, R.id.name_text, R.id.owner_text, R.id.address_text, R.id.price_text});
                            //实现列表的显示
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "查询失败:" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            HashMap<String, Object> data = (HashMap<String, Object>) listView.getItemAtPosition(position);
            String placeId = data.get("id").toString();
            dialog(placeId);
        }
    }

    protected void dialog(final String placeId) {
        new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")//设置对话框标题

                .setMessage("确认申请吗？")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        placeService.apply(userName, placeId, userId);
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                // TODO Auto-generated method stub
                Log.i("alertdialog", " 请保存数据！");
            }
        }).show();//在按键响应事件中显示此对话框
    }

}
