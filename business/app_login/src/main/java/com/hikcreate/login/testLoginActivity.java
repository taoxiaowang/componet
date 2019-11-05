package com.hikcreate.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.login.R;
import com.hikcreate.base.AppContext;
import com.hikcreate.data.config.AppProvider;
import com.hikcreate.module_router.LocalRouter;
import com.hikcreate.module_router.router.RouterRequest;
import com.hikcreate.module_router.router.RouterResponse;
import com.hikcreate.module_router.tools.ModuleRouterUtil;

/**
 * 类说明
 *
 * @author wangtao55
 * @date 2019/9/18
 * @mail wangtao55@hikcreate.com
 */
public class testLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lg_app_test);
        findViewById(R.id.mBtnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
