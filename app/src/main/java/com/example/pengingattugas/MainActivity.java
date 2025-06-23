package com.example.pengingattugas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private DatabaseHelper dbHelper;
    private EditText etNamaTugas, etMataKuliah;
    private TextView tvDeadline;
    private Calendar calendar;
    private TugasAdapter adapter;
    private ArrayList<Tugas> daftarTugas;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        etNamaTugas = findViewById(R.id.et_nama_tugas);
        etMataKuliah = findViewById(R.id.et_mata_kuliah);
        tvDeadline = findViewById(R.id.tv_deadline_terpilih);
        listView = findViewById(R.id.listview_tugas);

        Button btnPilihTanggal = findViewById(R.id.btn_pilih_tanggal);
        Button btnPilihWaktu = findViewById(R.id.btn_pilih_waktu);
        Button btnSimpanTugas = findViewById(R.id.btn_simpan_tugas);

        requestNotificationPermission();
        createNotificationChannel();

        btnPilihTanggal.setOnClickListener(v -> showDatePicker());
        btnPilihWaktu.setOnClickListener(v -> showTimePicker());
        btnSimpanTugas.setOnClickListener(v -> simpanTugas());

        loadTugasFromDB();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDeadlineText();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            updateDeadlineText();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDeadlineText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvDeadline.setText("Deadline: " + sdf.format(calendar.getTime()));
    }

    private void simpanTugas() {
        String namaTugas = etNamaTugas.getText().toString().trim();
        String mataKuliah = etMataKuliah.getText().toString().trim();
        long deadline = calendar.getTimeInMillis();

        if (namaTugas.isEmpty() || deadline <= System.currentTimeMillis()) {
            Toast.makeText(this, "Nama tugas dan deadline valid harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAMA_TUGAS, namaTugas);
        values.put(DatabaseHelper.COLUMN_MATA_KULIAH, mataKuliah);
        values.put(DatabaseHelper.COLUMN_DEADLINE, deadline);

        long newRowId = db.insert(DatabaseHelper.TABLE_TUGAS, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Tugas berhasil disimpan!", Toast.LENGTH_SHORT).show();
            scheduleNotification((int) newRowId, namaTugas, deadline);
            loadTugasFromDB();
            etNamaTugas.setText("");
            etMataKuliah.setText("");
            tvDeadline.setText("Deadline belum diatur");
        } else {
            Toast.makeText(this, "Gagal menyimpan tugas", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotification(int id, String namaTugas, long deadline) {
    Intent intent = new Intent(this, NotificationReceiver.class);
    intent.putExtra("NAMA_TUGAS", namaTugas);
    intent.putExtra("NOTIFICATION_ID", id);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline, pendingIntent);
        } else {
            Toast.makeText(this, "Aplikasi butuh izin untuk menyetel alarm presisi.", Toast.LENGTH_LONG).show();
            Intent intentSettings = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intentSettings);
        }
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline, pendingIntent);
    }
}

    private void loadTugasFromDB() {
        daftarTugas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TUGAS, null, null, null, null, null, DatabaseHelper.COLUMN_DEADLINE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaTugas = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_TUGAS));
                String matkul = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MATA_KULIAH));
                long deadline = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEADLINE));
                daftarTugas.add(new Tugas(id, namaTugas, matkul, deadline));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (adapter == null) {
            adapter = new TugasAdapter(this, daftarTugas, this::hapusTugas);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(daftarTugas);
            adapter.notifyDataSetChanged();
        }
    }

    private void hapusTugas(Tugas tugas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_TUGAS, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(tugas.getId())});

        cancelNotification(tugas.getId());

        Toast.makeText(this, "Tugas '" + tugas.getNamaTugas() + "' dihapus", Toast.LENGTH_SHORT).show();
        loadTugasFromDB();
    }

    private void cancelNotification(int id) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DeadlineTugasChannel";
            String description = "Channel untuk notifikasi deadline tugas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NotificationReceiver.CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }
}
