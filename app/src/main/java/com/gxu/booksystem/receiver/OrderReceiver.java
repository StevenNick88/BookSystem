package com.gxu.booksystem.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gxu.booksystem.R;

public class OrderReceiver extends BroadcastReceiver {

    private NotificationManager manager;
    private Notification.Builder builder;

    public OrderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        builder=new Notification.Builder(context);
//        builder.setContentTitle("������Ϣ��");
//        builder.setContentText(intent.getStringExtra("b_num"));
//        builder.setSmallIcon(R.drawable.dialog_icon);
//        manager.notify(1001,builder.build());

        String b_num=intent.getStringExtra("b_num");
        Toast.makeText(context,"有新消息了，编号为"+b_num+"的书籍已有用户还了，你可以前去借阅了",Toast.LENGTH_LONG).show();


    }
}
