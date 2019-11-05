package com.hikcreate.baidufacedect.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;
import com.hikcreate.baidufacedect.widget.DefaultDialog;

import java.util.HashMap;

public class FaceDetectExpActivity extends FaceDetectActivity {

    private DefaultDialog mDefaultDialog;

    public static void start(Context context){
        Intent intent = new Intent(context,FaceDetectExpActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            showMessageDialog("人脸图像采集", "采集成功");
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            showMessageDialog("人脸图像采集", "采集超时");
        }
    }

    private void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
    }

}
