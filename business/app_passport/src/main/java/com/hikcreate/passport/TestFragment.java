package com.hikcreate.passport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.app.passport.R;
import com.hikcreate.module_bus.bean.GeneralMessageBean;
import com.hikcreate.module_bus.driver.MessageWrap;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 类说明
 *
 * @author wangtao55
 * @date 2019/9/20
 * @mail wangtao55@hikcreate.com
 */
public class TestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageWrap.getMessageWrapDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //通过参数中的布局填充获取对应布局
        return inflater.inflate(R.layout.passport_test_fragment, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(GeneralMessageBean bean) {
        Toast.makeText(getActivity(), bean.getDataAction(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MessageWrap.getMessageWrapDefault().unregister(this);
    }
}
