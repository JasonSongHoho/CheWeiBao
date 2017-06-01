package cheweibao.jason.com.cheweibao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cheweibao.jason.com.carPlace.Bean.carPlace;
import cheweibao.jason.com.carPlace.service.placeService;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by JasonSong on 2017/5/1.
 */

public class PublishedActivity extends AppCompatActivity {
    private String userId;
    private String userName;
    //    private String placeId;
    private ListView listView;
    private SimpleAdapter adapter;
    private List<carPlace> carPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, "3447c5aa7fb9f5375bac2d0848471693");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published);
        init();
    }

    public void init() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        listView = (ListView) this.findViewById(R.id.listView_published);
        BmobQuery<carPlace> query = new BmobQuery<carPlace>();
        query.addWhereEqualTo("owner", userName);
        query.findObjects(new FindListener<carPlace>() {
            @Override
            public void done(List<carPlace> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
//                        Toast.makeText(getApplicationContext(), "有发布车位！",
//                                Toast.LENGTH_LONG).show();
                        carPlaces = object;
                        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                        if (carPlaces != null && carPlaces.size() > 0) {
                            for (carPlace carPlace : carPlaces) {
                                HashMap<String, Object> item = new HashMap<String, Object>();
                                item.put("id", carPlace.getObjectId());
                                item.put("name", carPlace.getName());
                                item.put("user", carPlace.getUser());
                                item.put("address", carPlace.getAddress());
                                item.put("price", carPlace.getPrice());
                                data.add(item);
                            }
                        }
                        //创建SimpleAdapter适配器将数据绑定到item显示控件上
                        adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.item,
                                new String[]{"id", "name", "user", "address", "price"}, new int[]{R.id.carid_text, R.id.name_text, R.id.owner_text, R.id.address_text, R.id.price_text});
                        //实现列表的显示
                        listView.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "没有发布车位！",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查看失败！",
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        //条目点击事件
        listView.setOnItemClickListener(new PublishedActivity.ItemClickListener());

        Button LoadButton = (Button) findViewById(R.id.load_but_published);
        LoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobQuery<carPlace> query = new BmobQuery<carPlace>();
                query.addWhereEqualTo("owner", userName);
                query.findObjects(new FindListener<carPlace>() {
                    @Override
                    public void done(List<carPlace> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                carPlaces = object;
                                List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                                if (carPlaces != null && carPlaces.size() > 0) {
                                    for (carPlace carPlace : carPlaces) {
                                        HashMap<String, Object> item = new HashMap<String, Object>();
                                        item.put("id", carPlace.getObjectId());
                                        item.put("name", carPlace.getName());
                                        item.put("user", carPlace.getUser());
                                        item.put("address", carPlace.getAddress());
                                        item.put("price", carPlace.getPrice());
                                        data.add(item);
                                    }
                                }
                                //创建SimpleAdapter适配器将数据绑定到item显示控件上
                                adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.item,
                                        new String[]{"id", "name", "user", "address", "price"}, new int[]{R.id.carid_text, R.id.name_text, R.id.owner_text, R.id.address_text, R.id.price_text});
                                //实现列表的显示
                                listView.setAdapter(adapter);

                            } else {
                                Toast.makeText(getApplicationContext(), "没有发布车位！",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "查看失败！",
                                    Toast.LENGTH_LONG).show();
                            Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
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
        new AlertDialog.Builder(PublishedActivity.this).setTitle("系统提示")//设置对话框标题

                .setMessage("确认取消发布吗？")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        placeService.cancelPublish(placeId, userId);
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