package cheweibao.jason.com.cheweibao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cheweibao.jason.com.carPlace.Bean.user;
import cheweibao.jason.com.carPlace.service.placeService;
import cheweibao.jason.com.carPlace.service.userService;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by JasonSong on 2017/4/18.
 */

public class RegisterActivity extends AppCompatActivity {
    String name;
    String password;
    static EditText mPasswordView;

    private EditText cityText;
    private EditText phoneText;
    private EditText carnumText;
    private EditText sexText;
    private EditText nicknameText;
    static String city;
    static String phone;
    static String carNum;
    static String sex;
    static String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, "3447c5aa7fb9f5375bac2d0848471693");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        mPasswordView = (EditText) findViewById(R.id.check_password);
        Button mEmailSignInButton = (Button) findViewById(R.id.check_password_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thisPassword = mPasswordView.getText().toString();
                if (thisPassword.equals(password)) {
                    city = cityText.getText().toString();
                    phone = phoneText.getText().toString();
                    carNum = carnumText.getText().toString();
                    sex = sexText.getText().toString();
                    nickname = nicknameText.getText().toString();
                    user user = new user();
                    user.setName(name);
                    user.setPassword(password);
                    user.setHavePlace(false);
                    user.setUsePlace(false);
                    user.setNickname(nickname);
                    user.setSex(sex);
                    user.setCity(city);
                    user.setPhone(phone);
                    user.setCarNum(carNum);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "注册成功！",
                                        Toast.LENGTH_SHORT).show();
                                registerLogin();
                                Log.i("bmob", "注册成功：" + objectId);
                            } else {
                                Toast.makeText(getApplicationContext(), "注册失败！：" + e.getMessage() + "," + e.getErrorCode(),
                                        Toast.LENGTH_LONG).show();
                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致！",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void registerLogin() {
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("name", name);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> object, BmobException e) {
                if (e == null) {
                    if (object.size() != 0 && password.equals(object.get(0).getPassword())) {
                        Toast.makeText(getApplicationContext(), "登录成功！",
                                Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent();
                        intent2.setClass(RegisterActivity.this, MainActivity.class);
                        intent2.putExtra("userId", object.get(0).getObjectId());
                        intent2.putExtra("name", name);
                        intent2.putExtra("password", password);
                        startActivity(intent2);
                        finish();
                    } else if (!password.equals(object.get(0).getPassword())) {
//                        Toast.makeText(getApplicationContext(), "password:"+password+"object.get(0)"+object.get(0),
//                                Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "登录失败：密码不正确！",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败：用户不存在！",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "登录失败" + e.getMessage() + e.toString(),
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public void init() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        cityText = (EditText) findViewById(R.id.check_city);
        phoneText = (EditText) findViewById(R.id.check_phone);
        carnumText = (EditText) findViewById(R.id.check_carNum);
        sexText = (EditText) findViewById(R.id.check_sex);
        nicknameText = (EditText) findViewById(R.id.check_nickname);
    }

}
