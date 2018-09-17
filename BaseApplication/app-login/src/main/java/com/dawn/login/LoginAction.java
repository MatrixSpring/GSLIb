package com.dawn.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drouter.api.action.IRouterAction;
import com.drouter.api.result.RouterResult;
import com.drouter.base.ThreadMode;
import com.drouter.base.annotation.Action;

import java.util.Map;

@Action(path = "login/action", threadMode = ThreadMode.MAIN)
public class LoginAction implements IRouterAction {

    @Override
    public RouterResult invokeAction(Context context, Map<String, Object> requestData) {
        Log.d("MainActivity","MainActivity : 999999");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("key", (String) requestData.get("key"));
        context.startActivity(intent);
        return new RouterResult.Builder().success().object(100).build();
    }
}
