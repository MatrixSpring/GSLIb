package com.dawn.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drouter.api.core.DRouter;
import com.drouter.api.result.ActionCallback;
import com.drouter.api.result.RouterResult;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String key = getIntent().getStringExtra("key");
        Toast.makeText(this, key, Toast.LENGTH_LONG).show();
    }

    public void click(View view){
        DRouter.getInstance()
                .action("circlemodule/test")
                .context(this)
                .param("key", "value")
                .invokeAction(new ActionCallback() {
                    @Override
                    public void onInterrupt() {
                        Log.e("TAG", "被拦截了");
                    }

                    @Override
                    public void onResult(RouterResult result) {
                        Log.e("TAG", "result = " + result.toString());
                    }
                });
    }

}
