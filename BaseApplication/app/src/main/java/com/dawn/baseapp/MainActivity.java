package com.dawn.baseapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.drouter.api.core.DRouter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public TextView tv_login;
    public TextView tv_user;
    public TextView tv_gesture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_login = (TextView) findViewById(R.id.message_login);
        tv_user = (TextView) findViewById(R.id.message_user);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_login:{
                Log.d("MainActivity","MainActivity : MainActivity");
                DRouter.getInstance()
                        .action("login/action")
                        .context(this)
                        .param("key", "value")
                        .invokeAction();
            }break;
            case R.id.message_user:{
                DRouter.getInstance()
                        .action("login/user")
                        .context(this)
                        .param("key", "value")
                        .invokeAction();
            }break;
            case R.id.message_gesture:{
                DRouter.getInstance()
                        .action("login/gesture")
                        .context(this)
                        .param("key", "value")
                        .invokeAction();
            }break;
        }
    }
}
