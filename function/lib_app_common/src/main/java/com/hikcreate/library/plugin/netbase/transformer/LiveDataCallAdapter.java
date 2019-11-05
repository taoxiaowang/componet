package com.hikcreate.library.plugin.netbase.transformer;

import android.arch.lifecycle.LiveData;

import com.hikcreate.library.util.LogCat;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LiveData转换适配器
 *
 * @author yslei
 * @date 2019/9/11
 * @email leiyongsheng@hikcreate.com
 */
public class LiveDataCallAdapter<R> implements CallAdapter<R, Object> {

    private final Type mResponseType;

    public LiveDataCallAdapter(Type responseType) {
        mResponseType = responseType;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public Object adapt(Call<R> call) {
        return new LiveData<R>() {
            private AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    LogCat.d("hik_SS", "live data enqueue ----");
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            LogCat.d("hik_SS", "live data enqueue response ----");
                            postValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable t) {
                            postValue(null);
                        }
                    });
                }
            }
        };
    }
}
