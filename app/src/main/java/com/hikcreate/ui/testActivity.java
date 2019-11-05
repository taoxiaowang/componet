package com.hikcreate.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.hikcreate.data.config.AppProvider;
import com.hikcreate.library.util.LogCat;
import com.hikcreate.module_router.router.RouterResponse;
import com.hikcreate.module_router.tools.ModuleRouterUtil;
import com.hikcreate.module_router.tools.RouterCallBack;

import com.module.hikcreate.R;

import java.util.HashMap;


/**
 * 类说明
 *
 * @author wangtao55
 * @date 2019/9/18
 * @mail wangtao55@hikcreate.com
 */
public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_test);
        findViewById(R.id.mBtnLogin).setOnClickListener(v -> {
            RouterResponse response = ModuleRouterUtil.router(getApplicationContext(),
                    AppProvider.LOGIN_PROVIDER, AppProvider.GET_USER_INFO_ACTION);
            if (response != null) {
                if (response.getObject() != null) {
                    HashMap value = (HashMap) response.getObject();
                    LogCat.d("userInfo", (String) value.get("mobile"));
                    LogCat.d("userInfo", (String) value.get("name"));
                }
                LogCat.d("userInfo", response.getData());
                Toast.makeText(testActivity.this, response.get(), Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.mBtnLoginSync).setOnClickListener(v -> {
            HashMap<String, String> value = new HashMap<>();
            value.put("name", "wangtao");
            value.put("mobile", "150000000000");
            ModuleRouterUtil.routeSyncToUi(getApplicationContext(),
                    AppProvider.LOGIN_PROVIDER, AppProvider.USER_INFO_CHANGE_ACTION, value, new RouterCallBack() {
                        @Override
                        public void callSuccess(RouterResponse response) {
                            Toast.makeText(testActivity.this, response.get(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void callFail(RouterResponse response) {
                            Toast.makeText(testActivity.this, response.get(), Toast.LENGTH_SHORT).show();
                        }
                    });


        });
    }
}
