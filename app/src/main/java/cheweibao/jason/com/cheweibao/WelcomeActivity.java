package cheweibao.jason.com.cheweibao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import a.We;

/**
 * Created by JasonSong on 2017/5/11.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);//你需要跳转的地方的代码
                finish();
            }
        }, 2000); //延迟2秒跳转
    }

}
