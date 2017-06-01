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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cheweibao.jason.com.carPlace.Bean.user;
import cheweibao.jason.com.carPlace.service.placeService;
import cheweibao.jason.com.carPlace.service.userService;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by JasonSong on 2017/5/1.
 */

public class UserInfoActivity extends AppCompatActivity {


    private String userName;
    private String pwd;
    private String userId;
    private String city;
    private String phone;
    private String carNum;
    private String sex;
    private String nickname;
    TextView idText;
    EditText nameEdit;
    EditText pwdEdit;
    EditText cityText;
    EditText phoneText;
    EditText carnumText;
    EditText sexText;
    EditText nicknameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Bmob.initialize(this, "3447c5aa7fb9f5375bac2d0848471693");
        init();
    }

    public void init() {
        idText = (TextView) findViewById(R.id.text_userid);
        nameEdit = (EditText) findViewById(R.id.edit_username);
        pwdEdit = (EditText) findViewById(R.id.edit_userpwd);
        cityText = (EditText) findViewById(R.id.edit_city);
        phoneText = (EditText) findViewById(R.id.edit_phone);
        carnumText = (EditText) findViewById(R.id.edit_carNum);
        sexText = (EditText) findViewById(R.id.edit_sex);
        nicknameText = (EditText) findViewById(R.id.edit_nickname);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userId = intent.getStringExtra("userId");
        pwd = intent.getStringExtra("password");
        idText.setText(userId);
        nameEdit.setText(userName);
        pwdEdit.setText(pwd);
        BmobQuery<user> query = new BmobQuery<user>();
        query.getObject(userId, new QueryListener<user>() {
            @Override
            public void done(user object, BmobException e) {
                if (e == null) {
                    city = object.getCity();
                    phone = object.getPhone();
                    carNum = object.getCarNum();
                    sex = object.getSex();
                    nickname = object.getNickname();
                    cityText.setText(city);
                    phoneText.setText(phone);
                    carnumText.setText(carNum);
                    sexText.setText(sex);
                    nicknameText.setText(nickname);
                } else {
                    Toast.makeText(getApplicationContext(), "查看失败！",
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        Button LoadButton = (Button) findViewById(R.id.but_update_userInfo);
        LoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = nameEdit.getText().toString();
                pwd = pwdEdit.getText().toString();
                city = cityText.getText().toString();
                phone = phoneText.getText().toString();
                carNum = carnumText.getText().toString();
                sex = sexText.getText().toString();
                nickname = nicknameText.getText().toString();
                userService.update(userId, userName, pwd, city, phone, carNum, sex, nickname);
                finish();
            }
        });


    }

}
