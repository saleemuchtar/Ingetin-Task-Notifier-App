package com.example.pengingattugas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "TugasDeadlineChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String namaTugas = intent.getStringExtra("NAMA_TUGAS");
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Deadline Tugas", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel untuk notifikasi deadline tugas");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Deadline Tugas!")
                .setContentText("Jangan lupa, tugas '" + namaTugas + "' akan segera berakhir.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(notificationId, builder.build());
    }
}