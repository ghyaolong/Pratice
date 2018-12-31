package item.com.pratice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.i("jxy",Thread.currentThread().getName()+" MySercice:onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("jxy",Thread.currentThread().getName()+" MyService:onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("jxy",Thread.currentThread().getName()+" MySercice:onDestroy");
        super.onDestroy();
    }

    public void mp3Play(String name){
        Log.i("jxy",this.getClass().getName()+"---->正在播放的歌曲名称是："+name);
    }
}
