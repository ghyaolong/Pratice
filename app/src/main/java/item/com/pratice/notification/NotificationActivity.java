package item.com.pratice.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import item.com.pratice.ImageActivity;
import item.com.pratice.R;

public class NotificationActivity extends AppCompatActivity {

    private Button btnNotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnNotify = findViewById(R.id.notify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationActivity.this,ImageActivity.class);
                PendingIntent pi = PendingIntent.getActivity(NotificationActivity.this,0,intent,0);

                //此种方法存在api不稳定情况，为了解决api兼容性问题，我们使用另一种方式
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(NotificationActivity.this,"default")
                        .setContentTitle("测试Title")
                        .setContentText("测试Content")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setVibrate(new long[]{0,1000,1000,1000})
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("测试Content,ddfdfdfdfd看接口接口垃圾到了房间阿里地方接口"
                                +"的克己复礼圣诞节开发两节课离开家撒到了房间啊点开始法律框架离开家楼上的房间啊了"
                                +"大风大浪房间看两节课 离开家垃圾了离开家垃圾离开家fdfdfdfd"))
                        .build();

                notificationManager.notify(1,notification);

            }
        });
    }
}
